import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Hamming {

    private static HashMap<String, String> syndrome;
    private static char[][] parityCheckMatrix;
    private static char[][] generatorMatrix;
    private static HashMap<String, String> hammingCodeTable;
    private static int n;
    private static int k;
    private static int d = 3;

    public static String encode(String data, int r) {

        StringBuilder paddedData = new StringBuilder(data);
        StringBuilder encoded = new StringBuilder();

        n = (int) Math.round(Math.pow(2, r)) - 1;
        k = (int) Math.round(Math.pow(2, r)) - 1 - r;

        parityCheckMatrix = getParityCheckMatrix();
        generatorMatrix = getGeneratorMatrix();
        buildSyndromes();

        while (paddedData.length() % k != 0) {
            paddedData.append("0");
        }
        data = paddedData.toString();
        System.out.println("data pieces");

        hammingCodeTable = new HashMap<String, String>();

        for(int i=0; i<data.length(); i+=k){
            String dataPiece = data.substring(i,i+k);
//            System.out.println(dataPiece);
            String hammingCode = encodeUsingGeneratorMatrix(dataPiece);
//            System.out.println(hammingCode);
            hammingCodeTable.put(hammingCode, dataPiece);
            encoded.append(hammingCode);
        }

        return encoded.toString();
    }

    public static String decode(String message, int r){
        StringBuilder decoded = new StringBuilder();
        System.out.println("Decoding");
        for(int i=0; i<message.length(); i+=n){
            String messagePiece = message.substring(i,i+n);
//            System.out.println("messagePiece: " + messagePiece);
            messagePiece = errorCorrect(messagePiece);
            decoded.append(hammingCodeTable.get(messagePiece));
        }
        return decoded.toString();
    }

    public static String errorCorrect(String message){
        String s = calculateOnParityCheckMatrix(message);
        String e = syndrome.get(s);
        System.out.println("s: " + s);
        System.out.println("e: " + e);
        StringBuilder correctedBuilder = new StringBuilder(message);
        int index = e.indexOf('1');
        if(index >=0){
            correctedBuilder.setCharAt(index, add('1', correctedBuilder.charAt(index)));
        }
        String corrected = correctedBuilder.toString();
        System.out.println("Corrected: " + corrected);
        return corrected;
    }

    public static char[][] getParityCheckMatrix(){
        List<String> nonZeroVector = new ArrayList<>();
        char[][] parityCheckMatrix = new char[n][n-k];

        System.out.println("n " + n + " k " + k);
        System.out.println("binary vector");

        for (int i = 1; i<=n; i++){
            if((i & (i-1)) != 0){
                String binaryVector = Integer.toBinaryString(i);
                while(binaryVector.length() < n-k){
                    binaryVector = "0" + binaryVector;
                }
                nonZeroVector.add(binaryVector);
                System.out.println(binaryVector);
            }
        }
////        System.out.println(nonZeroVector.size());
//        for(String v: nonZeroVector){
////            System.out.println(v);
//        }
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < n-k; j++) {
                parityCheckMatrix[i][j] = nonZeroVector.get(i).charAt(j);
            }
        }

        for (int i = k; i < n; i++) {
            for (int j = 0; j < n-k; j++) {
                if (i - k == j) {
                    parityCheckMatrix[i][j] = '1';
                } else {
                    parityCheckMatrix[i][j] = '0';
                }
            }
        }
        System.out.println(Arrays.deepToString(parityCheckMatrix));
        return parityCheckMatrix;
    }

    public static char[][] getGeneratorMatrix(){
        char[][] generatorMatrix = new char[k][n];
        for(int i=0; i<k; i++){
            for(int j=0; j<k; j++){
                if(i==j){
                    generatorMatrix[i][j] = '1';
                }else{
                    generatorMatrix[i][j] = '0';
                }
            }
        }

        for(int i=0; i<k; i++){
            for(int j=k; j<n; j++){
                generatorMatrix[i][j] = parityCheckMatrix[i][j-k];
            }
        }
        System.out.println(Arrays.deepToString(generatorMatrix));
        return generatorMatrix;
    }

    private static String encodeUsingGeneratorMatrix(String data){
        if(data.length() != generatorMatrix.length) return "";
        StringBuilder encodedMessage = new StringBuilder();
        for(int i=0; i<generatorMatrix[0].length; i++){
            char partialResult = multiply(data.charAt(0),generatorMatrix[0][i]);
            for(int j=1; j<generatorMatrix.length; j++){
                partialResult = add(partialResult,multiply(data.charAt(j),generatorMatrix[j][i]));
            }
            encodedMessage.append(partialResult);
        }
//        System.out.println(encodedMessage.toString());
        return encodedMessage.toString();
    }

    private static HashMap<String, String> buildSyndromes(){
        syndrome = new HashMap<String, String>();
        StringBuilder startBuilder = new StringBuilder();
        for(int i=0; i<n; i++){
            startBuilder.append('0');
        };
        String start = startBuilder.toString();
        String e = start;
        String s = calculateOnParityCheckMatrix(e);
        syndrome.put(s,e);
        System.out.println("e: " + e + ", s: " + s);
        for(int i=0; i<n; i++){
            startBuilder.setCharAt(i,'1');
            e = startBuilder.toString();
            startBuilder.setCharAt(i,'0');
            s = calculateOnParityCheckMatrix(e);
            syndrome.put(s,e);
            System.out.println("e: " + e + ", s: " + s);
        }
        return syndrome;
    }

    private static String calculateOnParityCheckMatrix(String e){
        StringBuilder result = new StringBuilder();
        if(e.length() != parityCheckMatrix.length) return "";
        for(int i=0; i<parityCheckMatrix[0].length; i++){
            char partialResult = multiply(e.charAt(0),parityCheckMatrix[0][i]);
            for(int j=1; j<parityCheckMatrix.length; j++){
                partialResult = add(partialResult,multiply(e.charAt(j),parityCheckMatrix[j][i]));
            }
            result.append(partialResult);
        }
        return result.toString();
    }

    private static char multiply(char a, char b){
        if(a == '1' && b == '1'){
            return '1';
        }else{
            return '0';
        }
    }

    private static char add(char a, char b){
        if((a == '1' && b == '0') || (a == '0' && b == '1')){
            return '1';
        }else{
            return '0';
        }
    }

    public static float getInformationRate(){
        return k/(n*1.0f);
    }

    public static void main(String[] args){
        StringBuilder sb = new StringBuilder(encode("101101000000000000000000000000000000000000000000000000000",6));
        System.out.println("encoded: " + sb.toString());
//        sb.setCharAt(4,'1');
        sb.setCharAt(4,'1');
        System.out.println("corrupted: " + sb.toString());
        System.out.println("decoded: " + decode(sb.toString(),6));
        System.out.println(getInformationRate());

    }
}
