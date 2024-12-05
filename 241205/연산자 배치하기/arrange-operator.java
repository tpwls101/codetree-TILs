import java.io.*;
import java.util.*;

/**
+ : 0 / - : 1 / x : 2
*/

public class Main {

    static int N; // 정수의 개수
    static int[] number; // 주어지는 정수
    static List<Integer> operator; // 연산자를 저장할 리스트(+:0 / -:1 / x:2)
    static boolean[] visited;
    static int[] arr; // 순열의 경우를 저장할 배열
    static int min = Integer.MAX_VALUE;
    static int max = Integer.MIN_VALUE;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());
        number = new int[N];
        operator = new ArrayList<>();
        visited = new boolean[N-1];
        arr = new int[N-1];

        st = new StringTokenizer(br.readLine());
        for(int i=0; i<N; i++) {
            number[i] = Integer.parseInt(st.nextToken());
        }

        st = new StringTokenizer(br.readLine());
        for(int i=0; i<3; i++) {
            int cnt = Integer.parseInt(st.nextToken());
            for(int j=0; j<cnt; j++) {
                operator.add(i);
            }
        }

        perm(0);

        System.out.println(min + " " + max);
    }

    static void perm(int cnt) {
        // 연산자 순열이 하나 결정되면 연산 시작
        if(cnt == N-1) {
            int num = operate();
            // 최대 최소 갱신
            min = Math.min(min, num);
            max = Math.max(max, num);
            return;
        }

        for(int i=0; i<operator.size(); i++) {
            if(visited[i]) continue;
            arr[cnt] = operator.get(i);
            visited[i] = true;
            perm(cnt+1);
            visited[i] = false;
        }
    }

    static int operate() {
        int num = number[0];
        for(int i=0; i<N-1; i++) {
            switch(arr[i]) {
                case 0: // +
                    num += number[i+1];
                    break;
                case 1: // -
                    num -= number[i+1];
                    break;
                case 2: // x
                    num *= number[i+1];
                    break;
            }
        }
        return num;
    }
}