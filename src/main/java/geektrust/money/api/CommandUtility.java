package geektrust.money.api;

public class CommandUtility {

    public static float[] deriveMarketChange(String[] marketChanges)
    {
        float[] result = new float[marketChanges.length];
        for (int i=0;i< marketChanges.length;i++)
        {
            result[i] = Float.parseFloat(marketChanges[i].substring(0, marketChanges[i].length()-1))/100f;
        }
        return result;
    }

}
