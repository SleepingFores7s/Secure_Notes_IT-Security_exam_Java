package utilities;

public class passwordHashing {

    /*
    !!!OBS!!!
    Only for educational use, do not use this in real world projects, as it will not be safe!

    I have NOT made this algorithm.
    I have taken help of AI to try and understand the hashing process,
        instead of using a library to just implement one directly.
    */

    public String hashPassword(String password) {

        byte[] bytes = toBytes(password);
        int state = process(bytes);
        return toHex(state);

    }

    //Turns password into a sequence of bytes
    private static byte[] toBytes(String input) {
        return input.getBytes();
    }

    //Process all bytes through mix
    private static int process(byte[] data) {
        int state = initialState();

        for (byte b : data) {
            state = mix(state, b);
        }
        return state;
    }

    //Arbitrary constant - Provides a starting value
    private static int initialState() {
        return 0xABCDEF01;
    }

    //
    private static int mix(int state, byte b) {
        state ^= b; //XOR mixes bits - XOR uses the actual byte numbers
        state = Integer.rotateLeft(state, 5); //Roatate bits
        state += 0x12345; // Add constant
        return state;
    }

    //Converts to a 8-char hex string
    private static String toHex(int value) {
        return String.format("%08x", value);
    }
}
