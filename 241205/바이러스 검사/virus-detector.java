import java.io.*;
import java.util.*;

/**
- 백준에 학급과 학생, 시험감독 나오는 문제랑 완전 같은 유형
- 입력값 범위 상 while문 쓰면 안되고, long 타입 써야 하는 것만 주의하면 쉬운 문제!
*/

public class Main {
    
    static int N; // 식당의 수
    static int[] customer; // 각 식당에 있는 고객의 수
    static int A; // 검사팀장이 검사할 수 있는 최대 고객 수
    static int B; // 검사팀원이 검사할 수 있는 최대 고객 수
    static long answer = 0; // 모두 검사하기 위한 검사자의 최소 수

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = null;

        N = Integer.parseInt(br.readLine());

        customer = new int[N];

        st = new StringTokenizer(br.readLine());
        for(int i=0; i<N; i++) {
            customer[i] = Integer.parseInt(st.nextToken());
        }

        st = new StringTokenizer(br.readLine());
        A = Integer.parseInt(st.nextToken());
        B = Integer.parseInt(st.nextToken());

        for(int i=0; i<N; i++) {
            customer[i] -= A;
            answer++; // 한 가게당 팀장은 무조건 필요
            if(customer[i] <= 0) continue;

            answer += customer[i] / B;
            if(customer[i] % B != 0) answer++;
        }

        System.out.println(answer);
    }
}
