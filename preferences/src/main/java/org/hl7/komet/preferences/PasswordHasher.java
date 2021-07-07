package org.hl7.komet.preferences;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 * {@link PasswordHasher}
 *
 * A safe, modern way to 1-way hash user passwords.
 * Adapted and enhanced from http://stackoverflow.com/a/11038230/2163960
 *
 * Later, added the ability to encrypt and decrypt arbitrary data - using many of the same
 * techniques.
 *
 * @author <a href="mailto:daniel.armbrust.list@gmail.com">Dan Armbrust</a>
 */
public class PasswordHasher {
    private record Salt<K extends Object, V extends Object> (K key, V value) {}

    private static Logger LOG = Logger.getLogger(PasswordHasher.class.getName());

    // The higher the number of ITERATIONS the more expensive computing the hash is for us and also for a brute force attack.
    private static final int ITERATIONS = 10 * 1024;

    private static final int SALT_LEN = 32;

    private static final int DESIRED_KEY_LENGTH = 256;

    private static final String KEY_FACTORY_ALGORITHM = "PBKDF2WithHmacSHA1";

    private static final String CIPHER_ALGORITHM = "PBEWithSHA1AndDESede/CBC/PKCS5Padding";

    private static final Random RANDOM = new Random();  //Note, it would be more secure to use SecureRandom... but the entropy issues on Linux are a nasty issue
    // and it results in SecureRandom.getInstance(...).generateSeed(...) blocking for long periods of time.  A regular random is certainly good enough  for our
    // encryption purposes.
    //private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * Checks whether given plaintext password corresponds to a stored salted hash of the password - such as
     * one generated by {@link #getSaltedHash(char[])}
     *
     * @param password the password
     * @param stored the stored
     * @return true, if successful
     * @throws Exception the exception
     */
    public static boolean check(char[] password, String stored)
            throws Exception {
        final String[] saltAndPass = stored.split("\\-\\-\\-");

        if (saltAndPass.length != 2) {
            return false;
        }

        if ((password == null) || (password.length == 0)) {
            return false;
        }

        final String hashOfInput = hash(password, Base64.getUrlDecoder().decode(saltAndPass[0]));

        return hashOfInput.equals(saltAndPass[1]);
    }

    /**
     * Decrypt data encrypted with the {@link #encrypt(char[], String)} method (which contains random salt)
     *
     * @param password The password to use to decrypt the data
     * @param encryptedData - The data to be decrypted
     * @return the byte array that was encrypted
     * @throws Exception the exception
     */
    public static byte[] decrypt(char[] password, String encryptedData)
            throws Exception {
        final long startTime	= System.currentTimeMillis();
        final int splitPoint = encryptedData.indexOf("---");
        if (splitPoint < 0) {
            throw new Exception("Invalid encrypted data, can't find salt.  Data was " + encryptedData);
        }

        final byte[] result = decrypt(password, Base64.getUrlDecoder().decode(encryptedData.substring(0, splitPoint)),
                encryptedData.substring((splitPoint + 3), encryptedData.length()));

        LOG.fine(String.format("Decrypt Time {} ms", System.currentTimeMillis() - startTime));
        return result;
    }

    /**
     * Decrypt data encrypted with the {@link #encrypt(char[], String)} method (which contains random salt)
     *
     * @param password The password to use to decrypt the data
     * @param encryptedData - The data to be decrypted
     * @return the value that was encrypted
     * @throws Exception the exception
     */
    public static String decryptToString(char[] password, String encryptedData)
            throws Exception {
        return new String(decrypt(password, encryptedData), "UTF-8");
    }

    /**
     * Decrypt data encrypted with the {@link #encrypt(char[], char[])} method (which contains random salt)
     *
     * @param password The password to use to decrypt the data
     * @param encryptedData - The data to be decrypted
     * @return the value that was encrypted
     * @throws Exception the exception
     */
    public static char[] decryptToChars(char[] password, String encryptedData) throws Exception {
        return toChars(decrypt(password, encryptedData));
    }

    private static char[] toChars(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        CharBuffer charBuffer = Charset.forName("UTF-8").decode(byteBuffer);
        char[] chars = Arrays.copyOfRange(charBuffer.array(), charBuffer.position(), charBuffer.limit());
        Arrays.fill(charBuffer.array(), '\u0000'); // clear sensitive data
        return chars;
    }

    /**
     * Decrypts data encrypted with the {@link #encrypt(char[], byte[], byte[])} where the user provided their own salt.
     *
     * @param password - The password to use for decryption
     * @param salt - the salt to use for decryption
     * @param data - The data to be decrypted
     * @return The decrypted data
     * @throws Exception
     */
    public static byte[] decrypt(char[] password, byte[] salt, String data)
            throws Exception {
        final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_FACTORY_ALGORITHM);
        final SecretKey key = keyFactory.generateSecret(new PBEKeySpec(password,
                salt,
                ITERATIONS,
                DESIRED_KEY_LENGTH));
        final Cipher pbeCipher = Cipher.getInstance(CIPHER_ALGORITHM);

        pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(salt, ITERATIONS));

        byte[] decrypted;

        try {
            decrypted = pbeCipher.doFinal(Base64.getUrlDecoder().decode(data));
        } catch (final BadPaddingException | IllegalBlockSizeException e) {
            throw new Exception("Invalid decryption password");
        }

        if (decrypted.length >= 40) {
            // The last 40 bytes should be the SHA1 Sum
            final String checkSum = new String(Arrays.copyOfRange(decrypted, decrypted.length - 40, decrypted.length));
            final byte[] userData = Arrays.copyOf(decrypted, decrypted.length - 40);
            final String computed = ChecksumGenerator.calculateChecksum("SHA1", userData);

            if (!Objects.equals(checkSum, computed)) {
                throw new Exception("Invalid decryption password, or truncated data");
            } else {
                return userData;
            }
        } else {
            throw new Exception("Truncated data");
        }
    }

    /**
     * Encrypt the provided byte array of data, using the provided password, with a random salt.
     * Calling this method twice with the same data and password will result in different hashes
     * due to the random salt.
     *
     * See {@link #decrypt(char[], String)} for a mechanism to decrypt the randomly salted data.
     *
     * @param password the password to encrypt with
     * @param data the data to be encrypted
     * @return The returned result will be URL safe per RFC 3986 with the embedded, encrypted data.
     * @throws Exception
     */
    public static String encrypt(char[] password, byte[] data)
            throws Exception {
        final long	startTime = System.currentTimeMillis();

        Salt<String, byte[]> salt = generateRandomSalt();
        // store the salt with the password
        final String result = salt.key() + "---" + encrypt(password, salt.value(), data);

        LOG.fine(String.format("Encrypt Time {} ms", System.currentTimeMillis() - startTime));
        return result;
    }

    /**
     * @return Generate URLsafe 64 encoded salt that does not contain the string "---"
     */
    private static Salt<String, byte[]> generateRandomSalt() {
        final byte[] salt = new byte[SALT_LEN];

        String result = null;
        //Make sure the generated salt doesn't contain the '---' that we use to split the salt on readback
        while (result == null || result.contains("---")) {
            RANDOM.nextBytes(salt);
            result = Base64.getUrlEncoder().encodeToString(salt);
        }
        return new Salt<>(result, salt);
    }

    /**
     * Calls {@link #encrypt(char[], byte[])} with the bytes encoded as UTF-8
     * @param password
     * @param data
     * @return the encrypted data
     * @throws Exception
     */
    public static String encrypt(char[] password, String data)
            throws Exception {
        return encrypt(password, data.getBytes("UTF-8"));
    }

    /**
     * Calls {@link #encrypt(char[], byte[])} with the bytes encoded as UTF-8
     * @param password
     * @param data
     * @return the encrypted data
     * @throws Exception
     */
    public static String encrypt(char[] password, char[] data)
            throws Exception {
        return encrypt(password, toBytes(data));
    }

    private static byte[] toBytes(char[] chars) {
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
        byte[] bytes = Arrays.copyOfRange(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit());
        Arrays.fill(byteBuffer.array(), (byte) 0); // clear sensitive data
        return bytes;
    }

    /**
     * This method is the same as {@link #encrypt(char[], byte[])} except that it allows the user to specify the salt
     *
     * This allows for consistent generation of encrypted data, if the user desires.
     *
     * See {@link #decrypt(char[], byte[], String)} for a mechanism to decrypt the initial data.
     *
     * @param password The password to encrypt with.
     * @param salt The salt to encrypt with
     * @param data the data to be encrypted.
     * @return The returned result will be URL safe per RFC 3986 with the embedded, encrypted data.
     * @throws Exception
     */
    public static String encrypt(char[] password, byte[] salt, byte[] data) throws Exception {
        final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_FACTORY_ALGORITHM);
        final SecretKey key = keyFactory.generateSecret(new PBEKeySpec(password, salt, ITERATIONS, DESIRED_KEY_LENGTH));
        final Cipher pbeCipher = Cipher.getInstance(CIPHER_ALGORITHM);

        pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(salt, ITERATIONS));

        // attach a sha1 checksum to the end of the data, so we know if we decrypted it properly.
        final byte[] dataCheckSum = ChecksumGenerator.calculateChecksum("SHA1", data).getBytes();
        final ByteBuffer temp = ByteBuffer.allocate(data.length + dataCheckSum.length);

        temp.put(data);
        temp.put(dataCheckSum);
        return Base64.getUrlEncoder().encodeToString(pbeCipher.doFinal(temp.array()));
    }

    /**
     * Computes a salted PBKDF2 hash of given plaintext password suitable for storing in a database.
     * Empty passwords are not supported.  Internally, uses a random salt as input for each hash,
     * so calling this method twice in a row with the same input will result in different hashes.
     *
     * See {@link #check(char[], String)} for a mechanism to validate a hash generated in this way.
     *
     * The returned hash will be URL safe per RFC 3986
     * @param password
     * @return the hashed data
     * @throws Exception
     */
    public static String getSaltedHash(char[] password) throws Exception {
        final long	startTime = System.currentTimeMillis();

        Salt<String, byte[]> salt = generateRandomSalt();

        // store the salt with the password
        final String result = salt.key() + "---" + hash(password, salt.value());

        LOG.fine(String.format("Compute Salted Hash time {} ms", System.currentTimeMillis() - startTime));
        return result;
    }


    /**
     * Computes a salted PBKDF2 hash of given plaintext password with the provided salt
     * Empty passwords are not supported.
     *
     * @param password the password
     * @param salt the salt
     * @return a URL Safe per RFC 3986
     * @throws Exception the exception
     */
    public static String hash(char[] password, byte[] salt)
            throws Exception {
        return hash(password, salt, ITERATIONS, DESIRED_KEY_LENGTH);
    }

    /**
     * Computes a salted PBKDF2 hash of given plaintext password with the provided salt
     * Empty passwords are not supported.
     *
     * @param password the password
     * @param salt the salt
     * @param iterationCount the iteration count
     * @param keyLength the key length
     * @return a URL Safe per RFC 3986
     * @throws Exception the exception
     */
    public static String hash(char[] password, byte[] salt, int iterationCount, int keyLength)
            throws Exception {
        final long startTime = System.currentTimeMillis();

        if ((password == null) || (password.length == 0)) {
            throw new IllegalArgumentException("Empty passwords are not supported.");
        }

        final SecretKeyFactory f		= SecretKeyFactory.getInstance(KEY_FACTORY_ALGORITHM);
        final SecretKey key = f.generateSecret(new PBEKeySpec(password, salt, iterationCount, keyLength));
        final String			  result = Base64.getUrlEncoder().encodeToString(key.getEncoded());

        LOG.fine(String.format("Password compute time: {} ms", System.currentTimeMillis() - startTime));
        return result;
    }

    /**
     * If the value appears to have been encrypted by one of our encrypt methods, decrypt the value using a password read from
     * one of these locations - in this order:
     *
     * The system property DECRYPTION_FILE - if this variable exists, it is assumed to contain the full path to a file. That
     * file is expected to contain a single line, which is the decryption password.
     *
     * The environment variable DECRYPTION_FILE - if this variable exists, it is assumed to contain the full path to a file. That
     * file is expected to contain a single line, which is the decryption password.
     *
     * If the variable is not set, then will check for the existence of a file named "decryption.password" in the JVM start location.
     * If that file exists, it will be expected to contain a single line, which is the decryption password.
     *
     * If no decryption password can be found, and the value appears to be encrypted, an exception will be thrown.
     *
     * If the value does NOT appear to have been encrypted by one of our encrypt methods, it will be returned, unchanged, without
     * attempting to find a decryption password. This allows clear-text passwords to be specified in config files for debugging.
     *
     * @param value The value to decrypt
     * @return The decrypted value, if it was encrypted, otherwise, the input value
     * @throws Exception if it appears to be encrypted, but a decryption password can't be found, or the decryption fails for any reason.
     */
    public static char[] decryptPropFileValueIfEncrypted(String value) throws Exception {
        if (value.isBlank()) {
            return new char[0];
        }
        final String[] saltAndPass = value.split("\\-\\-\\-");

        if (saltAndPass.length != 2) {
            LOG.fine("prop file value does not appear to be encrypted.  Returning");
            return value.toCharArray();
        }
        else {
            char[] master = getMasterPassword();

            if (master.length > 0) {
                return decryptToChars(master, value);
            }
            else {
                throw new Exception("The value appears to be encrypted, but no decryption password is available.  Please specify an enviornment variable"
                        + " of DECRYPTION_FILE which has a value that is an absolute path to a file that contains the decryption password, or, create the file "
                        + " " + new File("decryption.password").getAbsolutePath());
            }
        }
    }

    /**
     * Reads the master password from a file specified by the system property DECRYPTION_FILE - if this variable exists, it is assumed to contain the full
     * path to a file. That file is expected to contain a single line, which is the decryption password.
     *
     * If not yet found, read the master password from a file specified by the environment variable DECRYPTION_FILE - if this variable exists, it is assumed
     * to contain the full path to a file. That file is expected to contain a single line, which is the decryption password.
     *
     * If the variable is not set, then will check for the existence of a file named "decryption.password" in the JVM start location.
     * If that file exists, it will be expected to contain a single line, which is the decryption password.
     * If no file can be found, returns an empty char[]
     * @return the master password
     * @throws IOException if the file can't be read, or isn't a file.
     */
    public static char[] getMasterPassword() throws IOException
    {
        String decryptionFileLoc = System.getProperty("DECRYPTION_FILE");
        if (decryptionFileLoc == null) {
            decryptionFileLoc = System.getenv("DECRYPTION_FILE");
        }
        File defaultFileLoc = new File("decryption.password");
        if (!decryptionFileLoc.isBlank()) {
            File temp = new File(decryptionFileLoc);
            if (temp.isFile()) {
                return fileToCharArray(temp);
            }
            else {
                throw new IOException("The path specified by the enviornment variable 'DECRYPTION_FILE' is not a readable file");
            }
        }
        else if (defaultFileLoc.isFile()) {
            return fileToCharArray(defaultFileLoc);
        }
        return new char[] {};
    }

    /**
     * Read a file into a char[] without putting it into a string - useful for reading a password from a file.
     * @param file
     * @return the char[] data
     * @throws IOException
     */
    public static char[] fileToCharArray(File file) throws IOException
    {
        CharBuffer cb = CharBuffer.allocate(250);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8"))))  {
            int c;
            while ((c = reader.read()) != -1) {
                cb.append((char) c);
            }
        }
        cb.flip();
        char[] result = new char[cb.length()];
        for (int i = 0; i < result.length; i++) {
            result[i] = cb.get();
        }
        cb.clear();
        return result;
    }

    /**
     * Utility main for encrypting passwords on the command line, for storage in config files
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println("Enter 1 for a one-way hash, or 2 for a bi-directional hash, or 3 to decrypt an encrypted password");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int mode = Integer.parseInt(br.readLine().trim());
        switch (mode) {
            case 1:
            {
                System.out.println("Enter the password to be hashed: ");
                String pw = br.readLine();
                System.out.println("The hash is as follows.  Utilize this hash in combination with the 'check(...)' method in this class");
                System.out.println(getSaltedHash(pw.toCharArray()));
                break;
            }
            case 2:
            {
                System.out.println("Enter the value to be encrypted: ");
                String pw = br.readLine();
                System.out.println("Enter the password to use for the encryption / decryption: ");
                String decryptionPw = br.readLine();
                System.out.println("The encrypted password is as follows.  Utilize this in combination with the 'decrypt(...) method in this class'");
                System.out.println(encrypt(decryptionPw.toCharArray(), pw));
                break;
            }
            case 3:
            {
                System.out.println("Enter the value to be decrypted: ");
                String value = br.readLine();
                System.out.println("Enter the decryption password: ");
                String pw = br.readLine();
                System.out.println("The decrypted value is:");
                System.out.println(decryptToString(pw.toCharArray(), value));
                break;
            }
            default:
                break;
        }
    }
}