package jianzhi.排列;

/**
 * Created by shanyao on 2018/7/12.
 */
public class 数组全排列有重复数据 {
    public static void main(String[] args) {
        int a[] = {1,2,2};
        quanpailie(a,0,a.length - 1);
    }

    private static void quanpailie(int[] a, int begin,int end){
//        print(a,end);
        if (begin == end) {
//            System.out.println("-----");
            print(a,end);
//            System.out.println("-----");
        }
        for (int i = begin; i <= end;i++ ) {
            if (istSwith(a,i,end)) {//加个判断是否有相同的数据
                switchs(a, i, begin);
                quanpailie(a, begin + 1, end);
                switchs(a, begin, i);
            }
        }
    }

    private static void switchs(int[] a, int lo, int hi) {
        int temp = a[lo];
        a[lo] = a[hi];
        a[hi] = temp;
    }

    private static void print(int[] a,int len) {
        for (int i = 0; i <= len; i++) {
            System.out.print(a[i]);
        }
        System.out.println();
    }

    private static boolean istSwith(int [] a,int index,int len){
        for (int i = index + 1; i <=len; i++) {
            if (a[i] == a[index])
                return false;
        }
        return true;
    }
}
