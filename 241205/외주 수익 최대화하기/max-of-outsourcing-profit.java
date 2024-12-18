import java.io.*;
import java.util.*;

/**
- 백준 퇴사와 같은 유형의 문제
- 수익의 최댓값을 언제 갱신시켜주느냐를 잘 생각해야 함. if문에서 갱신시키면 기한이 맞지않아도 갱신되므로 주의!
- dfs 재귀호출할 때 start+arr[i][1]이 아니라, 현재 날짜인 i를 사용해야 함. i+arr[i][1]
*/

public class Main {
    
    static int N; // 휴가 날짜
    static int[][] arr; // 날짜, 기한, 수익 저장
    static int answer = 0;
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());

        arr = new int[N][3];

        for(int i=0; i<N; i++) {
            st = new StringTokenizer(br.readLine());
            arr[i][0] = i;
            arr[i][1] = Integer.parseInt(st.nextToken());
            arr[i][2] = Integer.parseInt(st.nextToken());
        }

        dfs(0, 0);

        System.out.println(answer);
    }

    // start : 시작날짜, sum : 수익의 합
    static void dfs(int start, int sum) {
        if(start > N) {
            return;
        }

        for(int i=start; i<N; i++) {
            dfs(i + arr[i][1], sum + arr[i][2]);
        }
        answer = Math.max(answer, sum); // 위치가 중요. if문 안에 있으면 안되는 경우도 answer로 갱신된다.
    }
}
