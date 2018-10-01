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

//�˰����� ����� �����ϱ� ���� Ŭ����
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

//�����߽� ���ϴ� �Լ�
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

// ODsay���� �ð��� �޾ƿ��� �����̹Ƿ�, ���⼭ �ɸ��� �ð��� �Է¹ްԲ� �ϸ� �� �� ����.
// ������ �ð��� ���߿� �Ÿ��� �ٲٸ� �� ��.

// �Ÿ� ����.
/*
// 3. �����߽��� �̿��Ͽ� �� ��ǥ���� ������ ��Į�� ���ϱ�. �� �� ������ �Ÿ� = ��Ʈ { (x1 - x2)^2 + (y1 - y2)^2 }
// 4. ��Į���� ũ���� ����ġ �ű��
double *calcWeight(User *user, User centerUser, int num) {
	//�ο� �� ��ŭ ����ġ �迭 �����Ҵ�
	double *weight = (double*)malloc(num * sizeof(double));
	// �߽��������� �� ����ڱ����� �Ÿ� ���� �迭 �����Ҵ�
	double *distance = (double*)malloc(num * sizeof(double));

	for (int i = 0; i < num; i++) {
		weight[i] = 0.1; //����ġ�� 1�� �ʱ�ȭ
		//�� ��ǥ���� �߽���ǥ������ �Ÿ��� ����
		distance[i] = sqrt( powl(user[i].getX() - centerUser.getX() , 2.0) + powl(user[i].getY() - centerUser.getY(), 2.0) );
	}

	// ������ ���� �Ÿ��� ���� ����ġ �ű�� (�Ÿ��� �� ���� ���� ����ġ ����)
	for (int i = 0; i < num; i++) {
		for (int j = 0; j < num; j++) {
			// i��° �Ÿ��� j��° �Ÿ����� ũ�� ����ġ ����.
			if (distance[i] > distance[j])
				weight[i] += 0.1;
		}
	}
	
	return weight;
}
*/

// �ð� ����. (���� ����ϴ� ����̶� ����ϰ� �ϱ�����, �ð��� �Է¹޾� �׿����� ����ġ�� �ű�)
double *calcWeight(User *user, User centerUser, int num) {
	//�ο� �� ��ŭ ����ġ �迭 �����Ҵ�
	double *weight = (double*)malloc(num * sizeof(double));
	// �߽��������� �� ����ڱ����� �ɸ��� �ð� ���� �迭 �����Ҵ�
	double *time = (double*)malloc(num * sizeof(double));

	for (int i = 0; i < num; i++) {
		weight[i] = 0.1; //����ġ�� 1�� �ʱ�ȭ
						 //�� ��ǥ���� �߽���ǥ������ �Ÿ��� ����
		cout << i + 1 << "��° ����ڰ� �߽���ǥ���� ���µ� �ɸ��� �ð� : ";
		cin >> time[i];
	}

	// ������ ���� �Ÿ��� ���� ����ġ �ű�� (�Ÿ��� �� ���� ���� ����ġ ����)
	for (int i = 0; i < num; i++) {
		for (int j = 0; j < num; j++) {
			// i��° �ɸ��� �ð��� j��° �ɸ��� �ð����� ũ�� ����ġ ����.
			if (time[i] > time[j])
				weight[i] += 0.1;
		}
	}

	return weight;
}

// 5. �� ���Ϳ� ����ġ��ŭ ���� ���� ���ϰ�, �� ���Ͱ��� ��ǥ�� ��Ÿ����

VectorXY getVector(double *weight, User *user, User centerUser, int num) {
	/*
	�߽�xy��ǥ - ����1�� xy��ǥ -> ����
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
	0. ����� �� �Է¹ޱ�
	1. �� ������� ��ǥ(x,y)�� �Է¹ޱ�.
	2. �����߽� ���ϱ�
	3. �����߽��� �̿��Ͽ� �� ��ǥ���� ������ ��Į�� ���ϱ�. �� �� ������ �Ÿ� = ��Ʈ { (x1 - x2)^2 + (y1 - y2)^2 }
	4. ��Į���� ũ���� ����ġ �ű��.
	5. �� ���Ϳ� ����ġ��ŭ ���� ���� ���ϰ�, �� ���Ͱ��� ��ǥ�� ��Ÿ����
	*/

	int num = 0;
	User *user;
	double *weight;

	// 0. ����� �� �Է� �ޱ�
	cout << "�� �� : ";
	cin >> num;

	user = (User*)malloc(num * sizeof(User));

	// 1. ����� ����ŭ ��ǥ �Է� �ޱ�
	for (int i = 0; i < num; i++) {
		double x = 0, y = 0;
		cout << i+1 << "��°  ������� x ��ǥ �Է� : ";
		cin >> x;
		cout << i+1 << "��°  ������� y ��ǥ �Է� : ";
		cin >> y;

		user[i].setXY(x, y);
	}

	// 2. �����߽� ���ϱ�
	User centerUser = getCenter(user, num);

	cout << "�����߽��� ��ǥ" << endl << "x : " << centerUser.getX() << "  y : " << centerUser.getY() << endl;

	// ����ġ �迭 �����ϱ�
	weight = calcWeight(user, centerUser, num);
	
	// ������ ����ġ �迭�� �Ѱ�, ���� ���� �ϱ�.
	VectorXY vector = getVector(weight, user, centerUser, num);

	cout << "�ð� ����ġ �˰����� �̿��Ͽ� ���� ����� ��ǥ" << endl << "x : " << vector.getX() << "  y : " << vector.getY() << endl;
	
	free(user);

	return 0;
}