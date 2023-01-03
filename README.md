# 🛠 제작중

## 프로세스

- **회원가입** 

![process1](https://user-images.githubusercontent.com/59686942/210375603-dc802c93-0fb5-4b30-aac7-0a4a5ea9aed4.gif)

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
- **로그인** 


 ![login](https://user-images.githubusercontent.com/59686942/203585556-a8694384-2385-4202-8972-a7783e225127.gif)
 
 
        - 카카오 로그인 -> Notion Api(DB) -> Notion Data Table -> App LiveData -> App View



- **커스텀 태그** 


 ![tag](https://user-images.githubusercontent.com/59686942/203588470-b12c00ee-4fdc-4413-b03e-89b590def44a.gif)

 
 
        - Custom Chip 으로 태그 안에 이모지를 넣을 수 있다.
 
 - **커스텀 Date Picker** 



 
 ![date](https://user-images.githubusercontent.com/59686942/203589405-22c3bdc0-e360-4795-949b-9ad4a3c4eb95.gif)

 
 
       - Date Picker Pull Custom 하여 UX적으로 더 접근하기 쉽게 만들었다.


 
 # 🛠 추가중입니다...
 
