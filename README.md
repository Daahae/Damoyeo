# 다모여
__사용자 중심의 시간 가중치 기반 중간지점 탐색 및 약속장소 추천 시스템__

## 개요
다수의 사람들과 만나고자 할 때, 각자 사는 지역이 다르기 때문에 약속장소를 정하기 힘들다. 정한다 하더라도, 모든 사람들이 만족할 만한 약속장소가 되긴 어렵다.

현재 출시되어 있는 앱들은 그 해결책을 사용자의 좌표 합을 평균 내어 중간지점을 구하는 방식으로 제공한다. 그러나 그러한 방식은 언뜻 보면 합리적인 것처럼 보이지만, 사용자간 거리가 같다고 걸리는 시간도 같지는 않다는 사실을 간과한 것이다.

그러므로 우리는 대중교통 길 찾기 정보를 기반으로 하여, 중간지점 까지 사용자들이 이동하는 '시간'을 공평하게 만들어줄 최적의 중간지점 탐색 알고리즘을 자체 제작하여 사용자에게 추천해주는 앱을 구현하였다.

본 프로젝트의 목표는 가장 핵심적인 기능, 사용자들 그룹에 이동 시간이 공평해지는 최적의 지점을 찾고 이를 대중교통 경로 정보와 함께 제공하는 것이다. 이로써 구성원들 간의 갈등을 줄이고, 편리하게 약속장소를 정할 수 있다.

 부가적으로 중간지점인근의 번화가를 추천해주는 랜드마크 기능을 제공하며, 중간지점과 랜드마크 지점 중 하나를 사용자가 선택하면 그 지점 인근의 만남 장소들을 카테고리 별로 분류하여 사용자에게 제공한다.
## 앱 이미지
* __중간지점 탐색 및 랜드마크 제공__
<div>
<img width="200"  src="https://user-images.githubusercontent.com/32588087/50564314-44e28800-0d67-11e9-8f47-8b1dae0594c7.png">

<img width="200"  src="https://user-images.githubusercontent.com/32588087/50564324-51ff7700-0d67-11e9-9263-97c6aed88167.png">

<img width="200"  src="https://user-images.githubusercontent.com/32588087/50564359-93902200-0d67-11e9-9fb8-8f11d1f66556.png">

<img width="200"  src="https://user-images.githubusercontent.com/32588087/50564461-609a5e00-0d68-11e9-8242-f9befacf2549.JPG">
</div>

---
* __장소추천 및 상세정보 제공__
<div>
<img width="200"  src="https://user-images.githubusercontent.com/32588087/50564391-d651fa00-0d67-11e9-8e9e-94869200a52a.png">

<img width="200"  src="https://user-images.githubusercontent.com/32588087/50564397-e36ee900-0d67-11e9-9445-f20fcef262fd.png">

<img width="200"  src="https://user-images.githubusercontent.com/32588087/50564422-19ac6880-0d68-11e9-905f-f329660e8334.png">
</div>

---
* __부가기능(지역검색)__
<div>
<img width="200"  src="https://user-images.githubusercontent.com/32588087/50564500-a6efbd00-0d68-11e9-890e-80724eed32c5.png">

<img width="200"  src="https://user-images.githubusercontent.com/32588087/50564507-b2db7f00-0d68-11e9-8f0c-189ec0701f0e.png">
</div>

## 최종결과
* __중간지점 적정성평가__
<div>
<img style="float:left;" width="240"  src="https://user-images.githubusercontent.com/32588087/50564598-79efda00-0d69-11e9-9fb6-5b5be9280cc0.jpg">

<img width="240"  src="https://user-images.githubusercontent.com/32588087/50564604-86743280-0d69-11e9-82ce-bc4424ac49f6.jpg">
</div>

다모여 서비스의 성능을 테스트하기 위하여 유사 서비스의 중간지점 선정 방식과 비교하였고, 사용될 데이터는 구글 길찾기를 통해 직접 각 출발 위치에 대해서 최적 중간지점을 구하는 방식으로 테스트 데이터를 수집하였다. 중간지점의 적정성의 판단 기준은 최적 통행 비용을 가지는 경우와, 그렇지 않을 때 실험데이터의 중간지점의 위치에서 반경 500m안에 알고리즘의 중간지점이 존재하는 경우로 나누었다.

어플이 선정해준 중간지점과 테스트 데이터와 비교했을 때, 유사 서비스의 방식이 34.6%, 다모여가 78.6%로 48%의 차이를 보이며 매우 높은 중간지점을 추천해 주었다. 두번째 표를 보면 실제 중간지점이 선정 되었을 경우의 통행비용과 교통정보를 알 수 있다.
