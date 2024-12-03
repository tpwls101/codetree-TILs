import java.io.*;
import java.util.*;

/**
1. 최적의 중심 좌표와 회전 횟수(각도) 구하기
	- 가능한 모든 경우를 후보 list에 저장
	- 조건에 맞게 정렬
	- 0번째가 최적
2. 최적의 중심 좌표에서 회전 횟수만큼 회전시키기
3. 유물 구하기 (가능할 때까지 반복)
	- BFS 탐색 (크기가 3 이상인 것만 획득 가능)
	- 제거하기 위해 remove 우선순위 큐에 담기
	- 우선순위에 따라 제거하면서 벽에 붙어 있는 유물 조각(큐 사용) poll()해서 사용
4. 한 턴에 가능한 최대 유물 가치 출력
*/

public class Main {
	
    static int K; // 탐사 반복 횟수
    static int M; // 벽면에 적힌 유물 조각의 개수
    static int[][] arr = new int[5][5]; // 유물 배열
    static int[][] copy; // 한번 회전시킨 후를 저장하는 배열
    static Queue<Integer> wall; // 벽면에 적힌 유물 조각 번호
    static List<Node> candidate; // 후보 중심 좌표 저장
    static PriorityQueue<Point> remove; // 제거할 유물의 좌표를 저장
	
    static int[] dx = { 0, 1, 0, -1 };
    static int[] dy = { 1, 0, -1, 0 };

    static class Node implements Comparable<Node> {
    	int x; // 중심 x좌표
    	int y; // 중심 y좌표
    	int score; // 유물 획득 가치
    	int rotateCnt; // 회전한 횟수
    	
    	Node(int x, int y, int score, int rotateCnt) {
    		this.x = x;
    		this.y = y;
    		this.score = score;
    		this.rotateCnt = rotateCnt;
    	}
    	
    	@Override
    	public int compareTo(Node o) {
    		if(this.score == o.score) {
    			if(this.rotateCnt == o.rotateCnt) {
    				if(this.y == o.y) {
    					return this.x - o.x; // 4. 회전 중심 좌표의 행 기준 오름차순
    				}
    				return this.y - o.y; // 3. 회전 중심 좌표의 열 기준 오름차순
    			}
    			return this.rotateCnt - o.rotateCnt; // 2. 회전한 각도 기준 오름차순
    		}
    		return o.score - this.score; // 1. 유물 획득 가치 기준 내림차순
    	}
    }
    
    static class Point implements Comparable<Point> {
    	int x;
    	int y;
    	
    	Point(int x, int y) {
    		this.x = x;
    		this.y = y;
    	}
    	
    	@Override
    	public int compareTo(Point o) {
    		if(this.y == o.y) {
    			return o.x - this.x; // 2. 행 기준 내림차순
    		}
    		return this.y - o.y; // 1. 열 기준 오름차순
    	}
    }
    
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        K = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        for(int i=0; i<5; i++) {
            st = new StringTokenizer(br.readLine());
            for(int j=0; j<5; j++) {
                arr[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        wall = new ArrayDeque<>();
        st = new StringTokenizer(br.readLine());
        for(int i=0; i<M; i++) {
            wall.add(Integer.parseInt(st.nextToken()));
        }
		
        // K번의 탐사 시작
		for(int exploration=0; exploration<K; exploration++) {
			
			candidate = new ArrayList<>();
			
			// 3x3 격자의 회전 중심 좌표 선택
			for(int i=1; i<=3; i++) {
				for(int j=1; j<=3; j++) {
					
					copy = new int[5][5];
					for(int k=0; k<5; k++) {
						copy[k] = arr[k].clone();
					}
					
					for(int k=1; k<=3; k++) { // 각도 : 90 180 270
						rotate(i-1, j-1); // 90도 회전						
						int score = bfs(copy); // 회전시킨 후 bfs를 통해 1차 유물 가치 구하기
						if(score > 0) { // 유물을 획득하는 경우에만 후보 리스트에 추가
							candidate.add(new Node(i, j, score, k));
						}
					}
				}
			}
			
			// 유물을 획득할 수 있는 방법이 없다면 탐사 즉시 종료
			if(candidate.size() == 0) break;
			
			// 최적의 회전 중심 좌표와 회전 횟수 결정
			Collections.sort(candidate);
			Node best = candidate.get(0);
			
			// rotate 함수가 copy 배열을 회전시키므로 copy 배열에 원본 배열을 다시 저장해 준다
			for(int i=0; i<5; i++) {
				copy[i] = arr[i].clone();
			}
			
			// 최적의 회전 중심 좌표 기준으로 회전 진행
			for(int i=0; i<best.rotateCnt; i++) {
				rotate(best.x - 1, best.y - 1);
			}
			
			// copy 배열을 arr 배열에 복사
			// 원래 배열에서 회전시킨 후 유물을 제거하고 새로 채워넣은 후 다음 탐사를 진행해야하기 때문
			for(int i=0; i<5; i++) {
				arr[i] = copy[i].clone();
			}
			
			int score = bfs(arr); // arr 배열의 유물 가치 구하기
			int sum = 0; // 유물 가치의 총합
			
			while(score > 0) {
				sum += score;
				removeAndFill();
				score = bfs(arr);
			}
			
			// 각 턴마다 획득한 유물의 가치의 총합 출력
			System.out.print(sum + " ");
		}
	}
	
	// 90도씩 회전시키는 메서드
	// copy 배열을 회전시킴
	static void rotate(int x, int y) { // (x, y)는 3x3 격자의 왼쪽 상단 좌표
		// 임시 배열을 만들어 90도 회전시킨 후의 값을 저장
		int[][] temp = new int[5][5];
		for(int i=0; i<5; i++) {
			temp[i] = copy[i].clone();
		}
		
		for(int i=0; i<3; i++) {
            temp[x+i][y+2] = copy[x][y+i];
        }
        for(int i=0; i<3; i++) {
            temp[x+2][y+2-i] = copy[x+i][y+2];
        }
        for(int i=0; i<3; i++) {
            temp[x+2-i][y] = copy[x+2][y+2-i];
        }
        for(int i=0; i<3; i++) {
            temp[x][y+i] = copy[x+2-i][y];
        }
        
        // temp 배열의 값을 다시 copy 배열에 저장
        for(int i=0; i<5; i++) {
        	copy[i] = temp[i].clone();
        }
	}
	
	// 중심 좌표에서 회전시킨 후 유물의 가치를 구하고, 유물이 사라질 좌표를 저장하는 메서드
	static int bfs(int[][] copy) {
		Queue<Point> queue;
		boolean[][] visited = new boolean[5][5];
		remove = new PriorityQueue<>();
		
		for(int i=0; i<5; i++) {
			for(int j=0; j<5; j++) {
				if(visited[i][j]) continue;
				
				queue = new ArrayDeque<>();
				queue.add(new Point(i, j));
				visited[i][j] = true;
				
				List<Point> list = new ArrayList<>();
				list.add(new Point(i, j));
				
				while(!queue.isEmpty()) {
					Point current = queue.poll();
					
					for(int k=0; k<4; k++) {
						int nx = current.x + dx[k];
						int ny = current.y + dy[k];
						
						if(nx >= 0 && nx < 5 && ny >= 0 && ny < 5) {
							if(copy[nx][ny] == copy[current.x][current.y] && !visited[nx][ny]) {
								queue.add(new Point(nx, ny));
								visited[nx][ny] = true;
								list.add(new Point(nx, ny));
							}
						}
					}
				}
				
				// 유물 조각이 3개 이상 연결된 경우에만 유물 가치 카운트
				if(list.size() >= 3) {
					remove.addAll(list); // 카운트하는 유물 조각을 remove 우선순위 큐에 모두 저장
				}
			}
		}
		return remove.size(); // 유물이 되어 사라질 조각들의 수(즉, 유물의 가치)
	}
	
	// 유물을 획득할 수 있으면 유물 조각을 제거하고 새로운 조각을 넣는 메서드
	static void removeAndFill() {
		while(!remove.isEmpty()) {
			Point current = remove.poll();
			arr[current.x][current.y] = wall.poll();
		}
	}

}
