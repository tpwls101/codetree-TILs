import java.io.*;
import java.util.*;

public class Main {

    static int N; // 무빙워크의 길이
    static int K; // 안정성이 0인 칸이 K개 이상이면 종료
    static Space[] arr;
    static int answer = 0; // 실험 횟수

    static class Space {
        int durability; // 내구성
        boolean existed; // 사람 존재 여부

        Space(int durability, boolean existed) {
            this.durability = durability;
            this.existed = existed;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        arr = new Space[2*N];

        st = new StringTokenizer(br.readLine());
        for(int i=0; i<2*N; i++) {
            int durability = Integer.parseInt(st.nextToken());
            arr[i] = new Space(durability, false);
        }

        while(true) {
            answer++;

            // 1. 무빙워크 한 칸 회전
            rotate();
            getOff(); // 마지막 칸에 사람이 있으면 즉시 내림

            // 2. 가장 먼저 무빙워크에 올라간 사람부터 한 칸씩 이동
            for(int i=N-2; i>=0; i--) {
                // 다음 칸에 사람이 없고 안정성이 0이 아니여야 가능
                if(arr[i].existed && !arr[i+1].existed && arr[i+1].durability > 0) {
                    arr[i].existed = false;
                    arr[i+1].existed = true; // 다음 칸으로 이동
                    arr[i+1].durability -= 1;
                }
            }
            getOff(); // 마지막 칸에 사람이 있으면 즉시 내림

            // 3. 사람 한 명 더 올리기
            if(!arr[0].existed && arr[0].durability != 0) {
                arr[0].existed = true;
                arr[0].durability -= 1;
            }

            // 안정성이 0인 칸이 K개 이상이면 종료
            int zeroCnt = 0;
            for(int i=0; i<2*N; i++) {
                if(arr[i].durability == 0) {
                    zeroCnt++;
                }
            }
            if(zeroCnt >= K) break;
        }
        
        System.out.println(answer);
    }

    static void rotate() {
        Space temp = arr[2*N-1];
        for(int i=2*N-1; i>0; i--) {
            arr[i] = arr[i-1];
        }
        arr[0] = temp;
    }

    static void getOff() {
        if(arr[N-1].existed) {
            arr[N-1].existed = false;
        }
    }
}