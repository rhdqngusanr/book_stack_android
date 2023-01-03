# 🛠 제작중

## 프로세스

- **회원가입** 

![process_reg](https://user-images.githubusercontent.com/59686942/210385611-8b1f1300-75ee-47aa-815d-439fa07c4a2f.gif)

(클릭하여 봐주시면 감사하겠습니다.)


        - 북스택 회원가입 프로세스 이며 자세한 설명은 아래에 있습니다.
## 설계

![image](https://user-images.githubusercontent.com/59686942/210356296-afa59ad6-23a6-42ed-8ccc-77103d81aaf5.png)


        - 노션으로 기획/설계 진행


![image](https://user-images.githubusercontent.com/59686942/210356370-52500a21-5aa7-4e0c-b03d-4accc792b663.png)


        - 아래 피그마링크에서 확인 가능

## UI/UX
 [UI/UX to figma 링크](https://www.figma.com/file/lncMBXq1YnEChiikLfVk3x/%EC%95%88%EB%8F%84%EA%B2%BD's-team-library?t=JoJb2M3bSG0Ybj6e-0) helpful ,design by JunChul LEE

## Developer
- **특징** 
  - 이 프로젝트의 DB는 일반적인 DB와 다른 방식으로 Notion 의 Data Table 과 Notion Api만을 사용하였다.
  - 기본적으로 DB 저장시  Notion Api(DB) -> Notion Data Table -> App LiveData -> App View 의 순서를 가지며 <br> Notion 에서 가져온 정보를 안드로이드 RoomDB로 저장하여 LiveData로 보여주는 MVVM 형식을 따르고 있다.
  - 아래의 모든 기능들은 DB와 통신을 한다.

- **Splash** 

![splash](https://user-images.githubusercontent.com/59686942/210377232-fcf2faac-cdeb-497b-b9dd-7022b7a0a8ca.gif)

        - Thema 형식의 Splash 로고화면과 info 페이지를 가지고있다.
        
        
- **로그인** 


![login](https://user-images.githubusercontent.com/59686942/210377590-4c9a7e75-097f-4281-91e6-ecdbc7b44f63.gif)
 
 
        - 카카오 로그인 -> Notion Api(DB) -> Notion Data Table -> App LiveData -> App View 형식을 가지고 있다
        - EditText 이벤트 , 키보드에 따라 변하는 버튼과 Text 기능들을 커스텀하였다.
        - 구글/카카오 로그인 모두 디버그키 릴리즈키를 등록하여 테스트하였다.



- **커스텀 태그** 


![tag](https://user-images.githubusercontent.com/59686942/210378563-51c6ed46-b0c8-4cdc-aabb-28bc67d92b72.gif)

 
 
        - ALL CUSTOM으로 태그 선택,추가 기능을 만들었다.
        - 사용자가 원하는 태그이름과 이미지를 선택하여 동적으로 태그를 추가가 가능하다.
        - 2중 어뎁터를 사용하여 이미지 선택 기능을 구현하였다.
  
 
 - **책 검색** 
 
 
 ![book](https://user-images.githubusercontent.com/59686942/210379451-667599b2-96d4-4f0d-b064-0855c3020a8b.gif)


       - 알라딘 API를 사용하여 책검색을 구현하였다. 
       - 알라딘 API가 가끔식 책의정보,작가가 공란일경우 따로 처리하였다.
 

 
 - **커스텀 Date Picker** 


![date](https://user-images.githubusercontent.com/59686942/210380776-30038816-7298-44fd-97d1-10fe0832a3e0.gif)

 
 
       - Date Picker Pull Custom 하여 UX적으로 더 접근하기 쉽게 만들었다.


 - **Highlight 추가** 


![add](https://user-images.githubusercontent.com/59686942/210381181-eebe2f9c-2de5-4297-8139-9c3edc6282e4.gif)


       - 태그 선택, 해제 , Comment 입력별 스크롤, 버튼처리를 하여 UX적으로 접근 하였다.
       
       
 - **수정/삭제** 


![modi](https://user-images.githubusercontent.com/59686942/210382185-3d0d22bb-9d25-455d-b5ff-203d740816b5.gif)



       - 수정 삭제 기능이다 변동사항은 ROOM DB, API 를 통해 DB로 바로 적용이된다.
 
 # 🛠 추가중입니다...
 
