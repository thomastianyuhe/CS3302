import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Thomas on 2017/11/7.
 */
public class Util {
    public static String generateRandomDataString(int size){
        List<Character> randomData = new ArrayList<Character>();
        Random randomGenerator = new Random();
        int numOfOne = randomGenerator.nextInt(size);
        for(int i=0; i<numOfOne; i++){
            randomData.add('1');
        }
        for(int j=numOfOne; j<size; j++){
            randomData.add('0');
        }
        Collections.shuffle(randomData);
        char[] randomCharArray = new char[randomData.size()];
        for(int i=0; i<randomCharArray.length; i++){
            randomCharArray[i] = randomData.get(i);
        }
        return new String(randomCharArray);
    }

    public static String contaminateMessage(String message, float p){
        StringBuilder messageBuilder = new StringBuilder(message);
        Random randomGenerator = new Random();
        for(int i=0; i<messageBuilder.length(); i++){
            float contaminate = randomGenerator.nextFloat();
            System.out.println(contaminate);
            if(contaminate < p){
                if(messageBuilder.charAt(i) == '0'){
                    messageBuilder.setCharAt(i, '1');
                }else{
                    messageBuilder.setCharAt(i, '0');
                }
            }
        }
        return messageBuilder.toString();
    }

    public static void main(String[] args){
        String message = generateRandomDataString(10);
        System.out.println(message);
        System.out.println(contaminateMessage(message, 0.2f));
    }
}
