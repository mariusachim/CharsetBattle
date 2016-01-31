import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

public class BaseTests {

    private static final Charset DEFAULT = Charset.defaultCharset();
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private static final Charset UTF_16 = Charset.forName("UTF-16");
    private static final Charset US_ASCII = Charset.forName("US-ASCII");
    private static final Charset GB18030 = Charset.forName("GB18030");

    private static final Charset ISO8859_1 = Charset.forName("ISO8859-1");


    @Test
    public void testDefaultCharset() {
        Assert.assertEquals(UTF_8, DEFAULT);
    }

    @Test
    public void testUTF8File8Encoding() {
        System.out.println("*** " + UTF_8 + " ***");
        checkFileInputs(UTF_8);
        System.out.println();
        System.out.println();
        Assert.assertEquals("中国", readAndDisplay("encoded_with_UTF8.txt", UTF_8));
    }

    @Test
    public void testUTF16FileEncoding() {
        System.out.println("*** " + UTF_16 + " ***");
        checkFileInputs(UTF_16);
        System.out.println();
        System.out.println();
        Assert.assertEquals("中国", readAndDisplay("encoded_with_UCS2_BE.txt", UTF_16));
    }

    @Ignore("Not worth testing ASCII since it is obsolete and does not encode all 'special' needed characters")
    @Test
    public void testASCIIFileEncoding() {
        System.out.print("*** " + US_ASCII + " ***");
        checkFileInputs(US_ASCII);
        System.out.println();
        System.out.println();
    }

    @Test
    public void testGB18030FileEncoding() {
        System.out.print("*** " + GB18030 + " ***");
        // checkFileInputs(GB18030);
        System.out.println();
        System.out.println();

        Assert.assertEquals("中国", readAndDisplay("encoded_with_GB2312.txt", GB18030));

    }

    @Test
    public void testInCodeStrings() throws CharacterCodingException {
        String cow = "\u725B";
        byte[] bytesInGB18030 = cow.getBytes(GB18030);
        String stringInGB18030 = new String(bytesInGB18030, GB18030);
        byte[] bytesInUTF8 = cow.getBytes(UTF_8);
        String stringInUTF8 = new String(bytesInUTF8, UTF_8);
        String stringInGBfromUTF8Bytes = new String(bytesInUTF8, GB18030);
    }


    @Test
    public void changeEncodingTest() {
        // TODO Change encoding of file from test
    }

    private void checkFileInputs(Charset charset) {
        readAndDisplay("encoded_with_UCS2_BE.txt", charset);
        readAndDisplay("encoded_with_UTF8.txt", charset);
        readAndDisplay("encoded_with_GB2312.txt", charset);
    }

    private String readAndDisplay(String fileName, Charset charset) {
        String returnMe = "";
        try {
            InputStream in = getClass().getResourceAsStream(fileName);
            List<String> defaultEncoded = IOUtils.readLines(in, DEFAULT);
            in = getClass().getResourceAsStream(fileName);
            List<String> charsetLines = IOUtils.readLines(in, charset);
            System.out.println("\t" + fileName + ": " + charsetLines);
            //Assert.assertEquals(typical.get(0), charsetLines.get(0));
            byte[] defaultEncBytes = defaultEncoded.get(0).getBytes();
            byte[] charsetBytes = charsetLines.get(0).getBytes(charset);
            System.out.println(DEFAULT + "\t: " + Arrays.toString(defaultEncBytes) + " - " + defaultEncBytes.length + " bytes");
            System.out.println(charset + "\t: " + Arrays.toString(charsetBytes) + " - " + charsetBytes.length + " bytes");
            returnMe = charsetLines.get(0);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
        return returnMe;
    }


}
