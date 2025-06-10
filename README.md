# ssg-order-api

> SSG 사전 과제 주문 API
>
> [목차]
>
> 1. 테이블 ERD 설계
> 2. 프로젝트 구조
> 3. 추후 확장 시 고려돼야할 부분

## 0. API 설명

- 주문생성: 상품번호 및 주문수량을 입력값으로 받아 주문을 생성합니다.
- 주문취소: 주문번호 및 상품번호를 입력값으로 받아 주문을 취소합니다.
- 주문조회: 주문번호를 입력값으로 받아 주문 목록을 반환합니다.

## 1. 테이블 ERD 설계

![image-20250610213148571](README.assets/image-20250610213148571.png)

#### 1.1 PD_PRODUCTS_BASE(상품 기본 테이블)

|     컬럼명     |   타입    |      설명      |
| :------------: | :-------: | :------------: |
|     PRD_SN     |  NUMBER   |       PK       |
|     PRD_NO     |  VARCHAR  |    상품번호    |
|    PRD_NAME    |  VARCHAR  |     상품명     |
|   SALE_PRICE   |  NUMBER   |    판매가격    |
| DISCOUNT_PRICE |  NUMBER   |    할인금액    |
|     STOCK      |  NUMBER   |      재고      |
|    CREATOR     |  VARCHAR  |  시스템생성자  |
|   CREATED_AT   | TIMESTAMP | 시스템생성시각 |
|    UPDATEDR    |  VARCHAR  |  시스템수정자  |
|   UPDATED_AT   | TIMESTAMP | 시스템수정시각 |

#### 1.2 PD_PRODUCTS_HISTORY(상품 이력 테이블)

> 재고 및 상품 변경 이력 관리용.
>
> 상품 변경 전의 상태를 snapshot 형태로 저장합니다.

|     컬럼명      |   타입    |     설명     |
| :-------------: | :-------: | :----------: |
| PRD_HISTORY_SN  |  NUMBER   |      PK      |
|     PRD_SN      |  NUMBER   |      FK      |
|     PRD_NO      |  VARCHAR  |   상품번호   |
|    PRD_NAME     |  VARCHAR  |    상품명    |
|   SALE_PRICE    |  NUMBER   |   판매가격   |
| DISCOUNT_PRICE  |  NUMBER   |   할인금액   |
|      STOCK      |  NUMBER   |     재고     |
|     CREATOR     |  VARCHAR  |    생성자    |
|   CREATED_AT    | TIMESTAMP |   생성시각   |
|    UPDATEDR     |  VARCHAR  |    수정자    |
|   UPDATED_AT    | TIMESTAMP |   수정시각   |
|  HIST_CREATOR   |  VARCHAR  |  이력생성자  |
| HIST_CREATED_AT | TIMESTAMP | 이력생성시각 |

#### 1.3 OD_ORDER_BASE(주문 기본 테이블)

> PK와 비즈니스키가 따로 있습니다.

|     컬럼명     |   타입    |       설명       |
| :------------: | :-------: | :--------------: |
|     ORD_SN     |  NUMBER   |        PK        |
|     ORD_NO     |  VARCHAR  |     주문번호     |
|   ORD_STATUS   |  VARCHAR  |     주문상태     |
| ORD_FIN_DTIME  | TIMESTAMP |   주문완료시각   |
| ORD_CNCL_DTIME | TIMESTAMP |   주문취소시각   |
| TOTAL_PAY_AMT  |  NUMBER   |    총결제금액    |
| CANCELABLE_AMT |  NUMBER   | 남은취소가능금액 |
|    USER_ID     |  VARCHAR  |   주문자아이디   |
|    CREATOR     |  VARCHAR  |      생성자      |
|   CREATED_AT   | TIMESTAMP |     생성시각     |
|    UPDATER     |  VARCHAR  |      수정자      |
|   UPDATED_AT   | TIMESTAMP |     수정시각     |

#### 1.4 OD_ORDER_DTL(주문 상세 테이블)

> OD_ORDER_BASE(주문기본테이블)과 1:N 관계에 있습니다.

|     컬럼명     |   타입    |     설명     |
| :------------: | :-------: | :----------: |
|   ORD_DTL_SN   |  NUMBER   |      PK      |
|     ORD_SN     |  NUMBER   |      FK      |
|     ORD_NO     |  VARCHAR  |   주문번호   |
|   ORD_DTL_NO   |  NUMBER   | 주문상세번호 |
|     PRD_NO     |  VARCHAR  |   상품번호   |
| ORD_DTL_STATUS |  VARCHAR  | 주문상세상태 |
| ORD_FIN_DTIME  | TIMESTAMP | 주문완료시각 |
| ORD_CNCL_DTIME | TIMESTAMP | 주문취소시각 |
|    ORD_QTY     |  NUMBER   |   주문수량   |
|   SALE_PRICE   |  NUMBER   |   판매가격   |
| DISCOUNT_PRICE |  NUMBER   |   할인금액   |
|    PAY_AMT     |  NUMBER   |   결제금액   |
|    CREATOR     |  VARCHAR  |    생성자    |
|   CREATED_AT   | TIMESTAMP |   생성시각   |
|    UPDATER     |  VARCHAR  |    수정자    |
|   UPDATED_AT   | TIMESTAMP |   수정시각   |

#### 1.5 OD_ORDER_DTL_HISTORY(주문상세 이력 테이블)

> 주문 상세 이력 관리용.
>
> 주문 상세 변경 전의 상태를 snapshot 형태로 저장합니다.

|     컬럼명      |   타입    |       설명       |
| :-------------: | :-------: | :--------------: |
| ORD_DTL_HIST_SN |  NUMBER   |        PK        |
|   ORD_DTL_SN    |  NUMBER   |        FK        |
|     ORD_SN      |  NUMBER   | 주문기본일련번호 |
|     ORD_NO      |  VARCHAR  |     주문번호     |
|   ORD_DTL_NO    |  NUMBER   |   주문상세번호   |
|     PRD_NO      |  VARCHAR  |     상품번호     |
| ORD_DTL_STATUS  |  VARCHAR  |   주문상세상태   |
|  ORD_FIN_DTIME  | TIMESTAMP |   주문완료시각   |
| ORD_CNCL_DTIME  | TIMESTAMP |   주문취소시각   |
|     ORD_QTY     |  NUMBER   |     주문수량     |
|   SALE_PRICE    |  NUMBER   |     판매가격     |
| DISCOUNT_PRICE  |  NUMBER   |     할인금액     |
|     PAY_AMT     |  NUMBER   |     결제금액     |
|     CREATOR     |  VARCHAR  |      생성자      |
|   CREATED_AT    | TIMESTAMP |     생성시각     |
|     UPDATER     |  VARCHAR  |      수정자      |
|   UPDATED_AT    | TIMESTAMP |     수정시각     |
|  HIST_CREATOR   |  VARCHAR  |    이력생성자    |
| HIST_CREATED_AT | TIMESTMAP |   이력생성시각   |

#### 1.6 OR_CLAIM(클레임 테이블)

|     컬럼명      |   타입    |      설명      |
| :-------------: | :-------: | :------------: |
|    CLAIM_SN     |  NUMBER   |       PK       |
|    CLAIM_NO     |  VARCHAR  |   클레임번호   |
|     ORD_NO      |  VARCHAR  |    주문번호    |
|   ORD_DTL_NO    |  NUMBER   |  주문상세번호  |
|   CLAIM_TYPE    |  VARCHAR  |   클레입타입   |
|  CLAIM_STATUS   |  VARCHAR  |   클레임상태   |
|  CLAIM_REASON   |  VARCHAR  |   클레임이유   |
|     PRD_NO      |  VARCHAR  |    상품번호    |
|    CLAIM_QTY    |  NUMBER   |   클레임수량   |
|   CLAIM_DTIME   | TIMESTAMP |   클레임시각   |
| CLAIM_FIN_DTIME | TIMESTAMP | 클레임완료시각 |
|   REFUND_AMT    |  NUMBER   |    환불금액    |
|     CREATOR     |  VARCHAR  |     생성자     |
|   CREATED_AT    | TIMESTAMP |    생성시각    |
|     UPDATER     |  VARCHAR  |     수정자     |
|   UPDATED_AT    | TIMESTAMP |    수정시각    |



## 2. 프로젝트 구조

#### 2.1 서비스 로직

📦 ssg_order_api
 ┣ 📂 common								공통클래스(예외, 유틸)
 ┃ ┣ 📂 util										주문번호 및 클레임번호 채번
 ┃ ┗ 📂 exception							비즈니스 예외 처리
 ┣ 📂 order										
 ┃ ┣ 📂 controller							주문 API 컨트롤러
 ┃ ┃┗ 📂 dto									API 요청/응답 DTO
 ┃ ┣ 📂 entity								   주문/주문상세 Entity
 ┃ ┣ 📂 repository						   주문 Repository
 ┃ ┗ 📂 service								 주문 서비스 로직
 ┣ 📂 product
 ┃ ┣ 📂 controller
 ┃ ┣ 📂 dto
 ┃ ┣ 📂 entity
 ┃ ┣ 📂 repository
 ┃ ┗ 📂 service
 ┣ 📂 claim
 ┃ ┣ 📂 dto
 ┃ ┣ 📂 entity
 ┃ ┣ 📂 repository
 ┃ ┗ 📂 service
 ┗ 📜 SsgOrderApiApplication.java

#### 2.2 테스트 로직

📦 test
 ┣ 📂 order
 ┃ ┗ 📜 OrderServiceTest.java       주문 생성 유닛 테스트
 ┣ 📂 claim
 	┗ 📜 ClaimServiceTest.java       주문 취소 (클레임) 유닛 테스트
