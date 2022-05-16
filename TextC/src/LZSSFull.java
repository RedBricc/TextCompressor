import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LZSSFull {
  static class LZSS {
    public static abstract class Block {
      public abstract byte getFlag();

      public abstract byte[] getBytes();

      public abstract String getRepresentation();
    }

    public static class CharacterBlock extends Block {
      private byte character;

      public CharacterBlock(byte character) {
        this.character = character;
      }

      @Override
      public byte getFlag() {
        return 0;
      }

      @Override
      public byte[] getBytes() {
        return new byte[] { character };
      }

      @Override
      public String getRepresentation() {
        return String.format("%s", (char) character);
      }
    }

    public static class ReferenceBlock extends Block {
      private int offset;
      private int length;

      public ReferenceBlock(int offset, int length) {
        this.offset = offset;
        this.length = length;
      };

      public static ReferenceBlock fromBytes(byte... bytes) {
        int offset = (((bytes[0] & 0xff) << 4) | (bytes[1] >> 4) & 0xf) & 0b1111_1111_1111;
        int length = bytes[1] & 0xf;
        return new ReferenceBlock(offset, length);
      }

      public int getOffset() {
        return (int) offset;
      }

      public int getLength() {
        return (int) length;
      }

      @Override
      public byte getFlag() {
        return 1;
      }

      @Override
      public byte[] getBytes() {
        byte b0 = (byte) ((offset >> 4) & 0xff);
        byte b1 = (byte) (((offset & 0xf) << 4) | (length & 0xf));
        return new byte[] { b0, b1 };
      }

      @Override
      public String getRepresentation() {
        return String.format("<%s,%s>", offset, length);
      }
    }

    public static class Utils {
      public static byte[] byteListToArray(List<Byte> list) {
        byte[] array = new byte[list.size()];
        Iterator<Byte> iterator = list.iterator();

        for (int i = 0; i < array.length; i++) {
          array[i] = iterator.next();
        }

        return array;
      }

      public static int indexOfSequence(List<Byte> buffer, List<Byte> ahead) {
        int foundBytes = 0;

        for (int i = 0; i < buffer.size(); i++) {
          if (buffer.get(i) == ahead.get(foundBytes)) {
            if (foundBytes + 1 == ahead.size()) {
              return buffer.size() - i + foundBytes;
            } else {
              foundBytes++;
            }
          } else {
            foundBytes = 0;
          }
        }

        return -1;
      }
    }

    public static class Constants {
      public static final int BITS_LENGTH = 4;
      public static final int BITS_OFFSET = 12;
      public static final int MIN_LENGTH = 4;

      public static final int MAX_LENGTH = (int) Math.pow(2, BITS_LENGTH);
      public static final int MAX_OFFSET = (int) Math.pow(2, BITS_OFFSET);

      public static final int WINDOW_SIZE = MAX_OFFSET;
      public static final int AHEAD_SIZE = MAX_LENGTH;

      public static final byte EOF = 0x00;
    }

    public static class ByteEncoder {
      public static byte[] encode(byte[] bytes) {
        // The search buffer
        List<Byte> buffer = new ArrayList<>();

        // The current element being searched
        List<Byte> ahead = new ArrayList<>();

        // The resulting blocks
        List<Block> blocks = new ArrayList<>();

        // Tracks if and where last elements were found
        boolean found = false;
        int lastIndex = -1;

        // Go through all characters
        for (int i = 0; i < bytes.length; i++) {
          // Build up ahead
          ahead.add(bytes[i]);

          if (ahead.size() < Constants.MIN_LENGTH)
            continue;

          int index = Utils.indexOfSequence(buffer, ahead);

          if ((index == -1 || ahead.size() <= Constants.MAX_LENGTH) && found) {
            int len = ahead.size() - 1;
            // Found full references
            blocks.add(new ReferenceBlock(lastIndex, len));
            ahead.clear();
            ahead.add(bytes[i]);
            found = false;
          } else if (index == -1) {
            // Match not found, add as literal
            blocks.add(new CharacterBlock(ahead.get(0)));
            buffer.add(ahead.get(0));
            if(buffer.size() > Constants.MAX_OFFSET-1) {
            	buffer.remove(0);
            }
            ahead.remove(ahead.get(0));
          } else {
            // Match found
            found = true;
            lastIndex = index;
          }
        }

        if (lastIndex != -1) {
          if (found == true) {
            // Found full reference
            blocks.add(new ReferenceBlock(lastIndex, ahead.size()));
          } else {
            // Match not found, add as literal
            for (byte b : ahead) {
              blocks.add(new CharacterBlock(b));
            }
          }
        }

        ArrayList<Byte> output = new ArrayList<>();
        ArrayList<Byte> blockBytes = new ArrayList<>();

        output.add((byte) 3);

        byte flags = 0;
        int flagsRead = 0;

        for (int i = 0; i < Math.ceil(blocks.size() / 8f) * 8; i++) {
          if (i < blocks.size()) {
            Block block = blocks.get(i);

            flags |= (byte) (block.getFlag() & 1) << flagsRead;
            flagsRead++;

            for (byte b : block.getBytes()) {
              blockBytes.add(b);
            }
          } else {
            flags |= 0 << flagsRead;
            flagsRead++;
            blockBytes.add(Constants.EOF);
          }

          if (flagsRead == 8) {
            output.add(flags);
            output.addAll(blockBytes);
            blockBytes.clear();
            flags = 0;
            flagsRead = 0;
          }
        }

        return Utils.byteListToArray(output);
      }

      public static byte[] decode(byte[] bytes) {
        ArrayList<Byte> output = new ArrayList<>();
        ArrayList<Byte> literals = new ArrayList<>();

        byte flags = 0;
        int flagsRead = 7;

        for (int i = 1; i < bytes.length; i++) {
          flags >>= 1;
          flagsRead++;

          if (flagsRead == 8) {
            flags = bytes[i++];
            flagsRead = 0;
          }

          if ((flags & 1) != 1 && bytes[i] == Constants.EOF) {
            break;
          }

          if ((flags & 1) == 0) {
            output.add(bytes[i]);
            literals.add(bytes[i]);
          } else {
            ReferenceBlock block = ReferenceBlock.fromBytes(bytes[i++], bytes[i]);

            int length = block.getLength();
            int offset = block.getOffset();
            int cur = literals.size();
            output.addAll(literals.subList(cur - offset, cur - offset + length));
          }
        }

        byte[] outputBytes = new byte[output.size()];
        for (int i = 0; i < outputBytes.length; i++) {
          outputBytes[i] = output.get(i);
        }

        return outputBytes;
      }
    }
  }

  public static void main(String[] args) {
    test(Path.of(".\\tests\\misc\\atest.txt"));
  }

  public static void test(Path filePath) {
    try {
      byte[] originalBytes = Files.readAllBytes(filePath);
      String originalString = new String(originalBytes, StandardCharsets.UTF_8);

      System.out.println("=".repeat(6));
      System.out.println("Original:");
      System.out.println("=".repeat(6));
      System.out.println(originalString.substring(0, Math.min(1024, originalString.length())));

      if (originalString.length() > 1024) {
        System.out.println("...");
      }

      byte[] encodedBytes = LZSS.ByteEncoder.encode(originalBytes);
      byte[] decodedBytes = LZSS.ByteEncoder.decode(encodedBytes);

      String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);

      System.out.println("=".repeat(6));
      System.out.println("Decoded:");
      System.out.println("=".repeat(6));
      System.out.println(decodedString.substring(0, Math.min(1024, decodedString.length())));

      if (decodedString.length() > 1024) {
        System.out.println("...");
      }

      System.out.println("=".repeat(6));
      System.out.printf("Original: %s, Encoded: %s, Decoded: %s\n",
          originalBytes.length, encodedBytes.length, decodedBytes.length);
      System.out.printf("Lossless: %s\n", originalString.equals(decodedString));
      System.out.printf("Compression rate: %.2f%%\n", encodedBytes.length / (float) originalBytes.length * 100);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
