import java.io.IOException;
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
        int offset = 0;

        for (int i = 0; i < buffer.size(); i++) {
          if (ahead.size() <= offset) {
            return i - ahead.size();
          }

          if (ahead.get(offset) == buffer.get(i)) {
            offset++;
          } else {
            offset = 0;
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

      public static final byte EOF = 0x04;
    }

    public static class ByteEncoder {
      public static byte[] encode(byte[] bytes) {
        // The search buffer
        List<Byte> buffer = new ArrayList<>();

        // The current element being searched
        List<Byte> ahead = new ArrayList<>();

        // The resulting blocks
        List<Block> blocks = new ArrayList<>();

        // Go through all characters
        for (int i = 0; i < bytes.length; i++) {
          byte charByte = bytes[i];

          // Temporary ahead buffer
          List<Byte> temp = new ArrayList<>(ahead) {
            {
              add(charByte);
            }
          };

          // Index of the longest repeating sequence in the array
          int index = Utils.indexOfSequence(buffer, temp);

          // Match wasn't found
          if (index == -1 || i == bytes.length - 1) {
            if (i == bytes.length - 1 && index != -1) {
              ahead.add(charByte);
            }

            if (ahead.size() > 1) {
              index = Utils.indexOfSequence(buffer, ahead);

              int length = ahead.size();
              int offset = i - index - length;

              if (length < Constants.MIN_LENGTH || length > Constants.MAX_LENGTH) {
                for (byte character : ahead) {
                  blocks.add(new CharacterBlock(character));
                }
              } else {
                blocks.add(new ReferenceBlock(offset, length));
              }

              buffer.addAll(ahead);
            } else {
              for (byte character : ahead) {
                blocks.add(new CharacterBlock(character));
              }

              buffer.addAll(ahead);
            }

            ahead.clear();
          }

          ahead.add(charByte);

          if (buffer.size() >= Constants.WINDOW_SIZE) {
            buffer.remove(0);
          }
        }

        ArrayList<Byte> output = new ArrayList<>();
        ArrayList<Byte> blockBytes = new ArrayList<>();

        byte flags = 0;
        int flagsRead = 0;

        for (int i = 0; i < blocks.size(); i++) {
          Block block = blocks.get(i);

          flags |= (byte) (block.getFlag() & 1) << flagsRead;
          flagsRead++;

          // if (i < 1024) {
          // System.out.print(block.getRepresentation());
          // }

          for (byte b : block.getBytes()) {
            blockBytes.add(b);

            if (i < 200) {
              if (block.getFlag() == 1) {
                // System.out.print(block.getRepresentation() + ": ");
              }
              // System.out.println(Integer.toHexString(Byte.toUnsignedInt(b)) + " " + (char)
              // b);
            }
          }

          if (flagsRead == 8) {
            if (i < 200) {
              // System.out
              // .println(String.format("%8s",
              // Integer.toBinaryString(Byte.toUnsignedInt(flags))).replace(' ', '0'));
            }

            output.add(flags);
            output.addAll(blockBytes);
            blockBytes.clear();
            flags = 0;
            flagsRead = 0;
          }
        }

        output.add(Constants.EOF);

        return Utils.byteListToArray(output);
      }

      public static byte[] decode(byte[] bytes) {
        ArrayList<Byte> output = new ArrayList<>();

        byte flags = 0;
        int flagsRead = 7;
        // System.out.println();
        for (int i = 0; i < bytes.length; i++) {
          flags >>= 1;
          flagsRead++;

          if (flagsRead == 8) {
            flags = bytes[i++];
            // System.out.println(String.format("%8s", Integer.toBinaryString((int)
            // flags)).replace(' ', '0'));
            flagsRead = 0;
          }

          if (i >= bytes.length) {
            break;
          }

          if ((flags & 1) == 0) {
            output.add(bytes[i]);
            // System.out.println(Integer.toHexString(Byte.toUnsignedInt(bytes[i])) + " " +
            // (char) bytes[i]);
          } else {
            ReferenceBlock block = ReferenceBlock.fromBytes(bytes[i++], bytes[i]);

            int length = block.getLength();
            int offset = block.getOffset();
            int cur = output.size();

            try {
              output.addAll(output.subList(cur - offset, cur - offset + length));
            } catch (Exception e) {

              for (byte c : output) {
                // System.out.print(Integer.toHexString(Byte.toUnsignedInt(c)) + " ");
              }
              String outString = new String(Utils.byteListToArray(output), StandardCharsets.UTF_8);

              System.out.println();
              System.out.println(outString);
              System.out.println(block.getRepresentation());
              for (byte c : outString.getBytes()) {
                // System.out.print(Integer.toHexString(Byte.toUnsignedInt(c)) + " ");
              }
              throw e;
            }
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
    test(Path.of("./File2.html"));
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

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
