package Utils;

public class TestCase {

    //nbEdge = 20 & recursiveCall = 1-5
    public static void testCase(int nbEdge, int recursiveCall){
        for(int i=1; i<recursiveCall; i++){
            TestCut t = new TestCut(nbEdge, i, "randomGraph_TestCase1_"+i+".txt", "result_TestCase1_"+i+".txt");
            t.doTest();
            t.exportResult();
        }
    }

}
