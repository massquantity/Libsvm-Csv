import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LibsvmToCsv {
    public static void main(String[] args) throws IOException {

        String src = args[0];
        String dest = args[1];

        double[][] table = EmptyTable(src);
        double[][] newcsv = NewCsv(table, src);
        write(newcsv, dest);
    }

    // 建立空表格， 维数为原数据集中最大特征维数
    public static double[][] EmptyTable(String src) throws IOException {
        int maxFeatures = 0, count = 0;
        File f = new File(src);
        BufferedReader br = new BufferedReader(new FileReader(f));
        String temp = null;
        while ((temp = br.readLine()) != null){
            count++;
            for (String pair : temp.split(" ")){
                int num = Integer.parseInt(pair.split(":")[0]);
                if (num > maxFeatures){
                    maxFeatures = num;
                }
            }
        }
        double[][] emptyTable = new double[count][maxFeatures + 1];
        return emptyTable;
    }

    public static double[][] NewCsv(double[][] newTable, String src) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(src)));
        String temp = null;
        int count = 0;
        while ((temp = br.readLine()) != null){
            String[] array = temp.split(" ");
            double label = Integer.parseInt(array[0]);
            for (String pair : Arrays.copyOfRange(array, 1, array.length)){
                String[] pairs = pair.split(":");
                int index = Integer.parseInt(pairs[0]);
                double value = Double.parseDouble(pairs[1]);
                newTable[count][index] = value;
            }
            newTable[count][0] = label;
            count++;
        }

        List<Integer> deleteCol = new ArrayList<>();  // 要删除的全 0 列
        int deleteColNum = 0;

        coll:
        for (int col = 0; col < newTable[0].length; col++){
            int zeroCount = 0;
            for (int row = 0; row < newTable.length; row++){
                if (newTable[row][col] != 0.0){
                    continue coll;  // 若有一个值不为 0， 继续判断下一列
                } else {
                    zeroCount++;
                }
            }

            if (zeroCount == newTable.length){
                deleteCol.add(col);
                deleteColNum++;
            }
        }

        int newColNum =  newTable[0].length - deleteColNum;
        double[][] newCsv = new double[count][newColNum];  // 新的不带全 0 列的空表格
        int newCol = 0;

        colll:
        for (int col = 0; col < newTable[0].length; col++){
            for (int dCol : deleteCol){
                if (col == dCol){
                    continue colll;
                }
            }

            for (int row = 0; row < newTable.length; row++){
                newCsv[row][newCol] = newTable[row][col];
            }
            newCol++;
        }
        return newCsv;
    }

    public static void write(double[][] table, String path) throws FileNotFoundException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path)));
        try{
            for (double[] row : table){
                int countComma = 0;
                for (double c : row){
                    countComma ++;
                    bw.write(String.valueOf(c));
                    if (countComma <= row.length - 1){
                        bw.append(',');
                    }
                }
                bw.flush();
                bw.newLine();
            }
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try{
                if (bw != null){
                    bw.close();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}

