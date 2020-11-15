import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class BTP implements Serializable {

    private byte[] objectName;
    private byte[] object;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        convert();
    }

    private static void convert() throws IOException {
        int i = 0;
        List<Path> list = Files.walk(Paths.get("src/main/resources/in")).filter(path -> path.toFile().isFile()).collect(Collectors.toList());

        for (Path path : list) {
            BTP btp = new BTP();
            btp.objectName = reverse(path.getFileName().toString().getBytes());
            btp.object = reverse(Files.readAllBytes(path));

            if (!Files.exists(Paths.get("src/main/resources/objects/"))) {
                Files.createDirectory(Paths.get("src/main/resources/objects/"));
            }

            FileOutputStream outputStream = new FileOutputStream("src/main/resources/objects/" + btp.objectName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(btp);

            Files.delete(path);
            outputStream.close();
            objectOutputStream.close();
        }

    }

    private static void unConvert() throws IOException, ClassNotFoundException {
        List<Path> list = null;
        list = Files.walk(Paths.get("src/main/resources/objects")).filter(path -> path.toFile().isFile()).collect(Collectors.toList());

        for (Path path : list) {
            FileInputStream fileInputStream = new FileInputStream(String.valueOf(path));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            BTP btp = (BTP) objectInputStream.readObject();
            Files.write(Paths.get("src/main/resources/out/" + new String(reverse(btp.objectName))), reverse(btp.object));

            fileInputStream.close();
            objectInputStream.close();
        }
    }

    private static byte[] reverse(byte[] b) {
        byte[] result = new byte[b.length];
        for (int i = 0, j = b.length - 1; i < b.length; i++, j--) {
            result[i] = b[j];
        }
//        for (int i = 0, j = b.length - 1; i < b.length; i++, j--) {
//            System.out.println(result[j] + " - " + b[i]);
//        }
        return result;
    }


}
