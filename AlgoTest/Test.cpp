#include <iostream>
#include <malloc.h>
#include <math.h>
using namespace std;

class User {
	double x, y;
public : 
	User() {
		x = 0;
		y = 0;
	}

	void setXY(double uX, double uY) {
		x = uX;
		y = uY;
	}

	double getX() {
		return x;
	}

	double getY() {
		return y;
	}

};

//알고리즘의 결과를 저장하기 위한 클래스
class VectorXY {
	private : 
		double vX, vY;
	public : 
		void setXY(double x, double y) {
			vX = x;
			vY = y;
		}

		double getX() {
			return vX;
		}

		double getY() {
			return vY;
		}
};

//무게중심 구하는 함수
User getCenter(User *user, int count) {
	double centerX = 0;
	double centerY = 0;
	double sumX = 0;
	double sumY = 0;
	User centerUser;

	for (int i = 0; i < count; i++) {
		sumX += user[i].getX();
		sumY += user[i].getY();
	}

	centerX = sumX / count;
	centerY = sumY / count;

	centerUser.setXY(centerX, centerY);

	return centerUser;
}

// ODsay에서 시간을 받아오는 형식이므로, 여기서 걸리는 시간을 입력받게끔 하면 될 것 같음.
// 여기의 시간을 나중에 거리로 바꾸면 될 것.

// 거리 기준.
/*
// 3. 무게중심을 이용하여 각 좌표와의 벡터의 스칼라값 구하기. 두 점 사이의 거리 = 루트 { (x1 - x2)^2 + (y1 - y2)^2 }
// 4. 스칼라값의 크기대로 가중치 매기기
double *calcWeight(User *user, User centerUser, int num) {
	//인원 수 만큼 가중치 배열 동적할당
	double *weight = (double*)malloc(num * sizeof(double));
	// 중심지점에서 각 사용자까지의 거리 저장 배열 동적할당
	double *distance = (double*)malloc(num * sizeof(double));

	for (int i = 0; i < num; i++) {
		weight[i] = 0.1; //가중치를 1로 초기화
		//각 좌표에서 중심좌표까지의 거리를 구함
		distance[i] = sqrt( powl(user[i].getX() - centerUser.getX() , 2.0) + powl(user[i].getY() - centerUser.getY(), 2.0) );
	}

	// 위에서 나온 거리를 토대로 가중치 매기기 (거리가 멀 수록 높은 가중치 받음)
	for (int i = 0; i < num; i++) {
		for (int j = 0; j < num; j++) {
			// i번째 거리가 j번째 거리보다 크면 가중치 증가.
			if (distance[i] > distance[j])
				weight[i] += 0.1;
		}
	}
	
	return weight;
}
*/

// 시간 기준. (실제 사용하는 방법이랑 비슷하게 하기위해, 시간을 입력받아 그에따른 가중치를 매김)
double *calcWeight(User *user, User centerUser, int num) {
	//인원 수 만큼 가중치 배열 동적할당
	double *weight = (double*)malloc(num * sizeof(double));
	// 중심지점에서 각 사용자까지의 걸리는 시간 저장 배열 동적할당
	double *time = (double*)malloc(num * sizeof(double));

	for (int i = 0; i < num; i++) {
		weight[i] = 0.1; //가중치를 1로 초기화
						 //각 좌표에서 중심좌표까지의 거리를 구함
		cout << i + 1 << "번째 사용자가 중심좌표까지 오는데 걸리는 시간 : ";
		cin >> time[i];
	}

	// 위에서 나온 거리를 토대로 가중치 매기기 (거리가 멀 수록 높은 가중치 받음)
	for (int i = 0; i < num; i++) {
		for (int j = 0; j < num; j++) {
			// i번째 걸리는 시간이 j번째 걸리는 시간보다 크면 가중치 증가.
			if (time[i] > time[j])
				weight[i] += 0.1;
		}
	}

	return weight;
}

// 5. 각 벡터에 가중치만큼 곱한 값을 구하고, 그 벡터값의 좌표를 나타내기

VectorXY getVector(double *weight, User *user, User centerUser, int num) {
	/*
	중심xy좌표 - 유저1의 xy좌표 -> 저장
	*/
	VectorXY vector;
	double vectorRX = 0;
	double vectorRY = 0;

	for (int i = 0; i < num; i++) {
		vectorRX += (weight[i] * ( user[i].getX() - centerUser.getX() ) );
		vectorRY += (weight[i] * (user[i].getY() - centerUser.getY() ) );
	}
	vector.setXY(vectorRX, vectorRY);

	return vector;
}

int main() {

	/*
	0. 사용자 수 입력받기
	1. 각 사용자의 좌표(x,y)를 입력받기.
	2. 무게중심 구하기
	3. 무게중심을 이용하여 각 좌표와의 벡터의 스칼라값 구하기. 두 점 사이의 거리 = 루트 { (x1 - x2)^2 + (y1 - y2)^2 }
	4. 스칼라값의 크기대로 가중치 매기기.
	5. 각 벡터에 가중치만큼 곱한 값을 구하고, 그 벡터값의 좌표를 나타내기
	*/

	int num = 0;
	User *user;
	double *weight;

	// 0. 사용자 수 입력 받기
	cout << "몇 명 : ";
	cin >> num;

	user = (User*)malloc(num * sizeof(User));

	// 1. 사용자 수만큼 좌표 입력 받기
	for (int i = 0; i < num; i++) {
		double x = 0, y = 0;
		cout << i+1 << "번째  사용자의 x 좌표 입력 : ";
		cin >> x;
		cout << i+1 << "번째  사용자의 y 좌표 입력 : ";
		cin >> y;

		user[i].setXY(x, y);
	}

	// 2. 무게중심 구하기
	User centerUser = getCenter(user, num);

	cout << "무게중심의 좌표" << endl << "x : " << centerUser.getX() << "  y : " << centerUser.getY() << endl;

	// 가중치 배열 저장하기
	weight = calcWeight(user, centerUser, num);
	
	// 저장한 가중치 배열을 넘겨, 벡터 연산 하기.
	VectorXY vector = getVector(weight, user, centerUser, num);

	cout << "시간 가중치 알고리즘을 이용하여 구한 결과의 좌표" << endl << "x : " << vector.getX() << "  y : " << vector.getY() << endl;
	
	free(user);

	return 0;
}