import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/3
 * Time: 18:24
 * To change this template use File | Settings | File Templates.
 */
public class IDTester {
    public static void main(String[] args) {
        AtomicInteger idMaker = new AtomicInteger(0);
        while (true){
            int i = idMaker.incrementAndGet();
            if(i%(10000*10000*10000)==0){
                System.out.println(i);
            }

        }
    }
}
