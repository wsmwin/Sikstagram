package com.example.sikstagram;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;


public class display_plant_info extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_plant);

        StrictMode.enableDefaults();

        TextView t1 = (TextView)findViewById(R.id.plntzrNm); //파싱된 결과확인!
        TextView t2 = (TextView)findViewById(R.id.postngplaceCodeNm); //파싱된 결과확인!
        TextView t3 = (TextView)findViewById(R.id.prpgtEraInfo); //파싱된 결과확인!
        TextView t4 = (TextView)findViewById(R.id.prpgtmthCodeNm); //파싱된 결과확인!
        TextView t5 = (TextView)findViewById(R.id.soilInfo); //파싱된 결과확인!
        TextView t6 = (TextView)findViewById(R.id.speclmanageInfo); //파싱된 결과확인!
        TextView t7 = (TextView)findViewById(R.id.toxctyInfo); //파싱된 결과확인!
        TextView t8 = (TextView)findViewById(R.id.winterLwetTpCodeNm); //파싱된 결과확인!

        ImageView img_v = (ImageView)findViewById(R.id.img);

        // API에서 가져 올 식물 특징
        //plntzrNm 식물명
        //postngplaceCodeNm 배치장소
        //prpgtEraInfo 번식시기정보
        //prpgtmthCodeNm 번식방법
        //soilInfo 토양정보
        //speclmanageInfo 특별관리 정보
        //toxctyInfo 녹성정보
        //winterLwetTpCodeNm 겨울 최저 온도


        boolean  isplntzrNm=false, ispostngplaceCodeNm=false,isprpgtEraInfo=false,isprpgtmthCodeNm=false,issoilInfo=false,isspeclmanageInfo=false,istoxctyInfo=false,iswinterLwetTpCodeNm =  false;

        String plntzrNm= null,postngplaceCodeNm= null,prpgtEraInfo= null,prpgtmthCodeNm= null,soilInfo= null, speclmanageInfo= null,toxctyInfo= null,winterLwetTpCodeNm= null;

        //12938
        //13186
        Intent intent =getIntent();

        String cntntsNO=intent.getExtras().getString("pltCode");







        //cntntsNO가 0 이면 DEFAULT인 경우. 즉, LISTVIEW를 통해 선택해서 사진 및 정보
        //cntntsNO 특정 숫자이면


        String img_url="";


        try{


            ImageUrl_Convertor converter = new ImageUrl_Convertor();
            img_url=converter.getUrl(cntntsNO);

            GetBitmapImageFromUrl get_bitmap = new GetBitmapImageFromUrl();
            Bitmap res = get_bitmap.getImageBitmap(img_url);

            img_v.setImageBitmap(res);



        }
        catch(Exception e)
        {
            System.out.println(e);
        }




        try{
            URL url = new URL("http://api.nongsaro.go.kr/service/garden/gardenDtl?"
                    + "&apiKey=20191031ZDQ1B8W3K6S6Z1PUAXHNW"
                    + "&cntntsNo="+cntntsNO);
            //검색 URL부분

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            System.out.println("파싱시작합니다.");
            while (parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if(parser.getName().equals("plntzrNm")) { //title 만나면 내용을 받을수 있게 하자
                            isplntzrNm = true;
                        }
                        if(parser.getName().equals("postngplaceCodeNm")) { //title 만나면 내용을 받을수 있게 하자
                            ispostngplaceCodeNm = true;
                        }
                        if(parser.getName().equals("prpgtEraInfo")) { //title 만나면 내용을 받을수 있게 하자
                            isprpgtEraInfo = true;
                        }
                        if(parser.getName().equals("prpgtmthCodeNm")) { //title 만나면 내용을 받을수 있게 하자
                            isprpgtmthCodeNm = true;
                        }
                        if(parser.getName().equals("soilInfo")) { //title 만나면 내용을 받을수 있게 하자
                            issoilInfo = true;
                        }
                        if(parser.getName().equals("speclmanageInfo")) { //title 만나면 내용을 받을수 있게 하자
                            isspeclmanageInfo = true;
                        }
                        if(parser.getName().equals("toxctyInfo")) { //title 만나면 내용을 받을수 있게 하자
                            istoxctyInfo = true;
                        }
                        if(parser.getName().equals("winterLwetTpCodeNm")) { //title 만나면 내용을 받을수 있게 하자
                            iswinterLwetTpCodeNm = true;
                        }
                        if(parser.getName().equals("message")){ //message 태그를 만나면 에러 출력

                            //여기에 에러코드에 따라 다른 메세지를 출력하도록 할 수 있다.
                        }
                        break;
                    case XmlPullParser.TEXT://parser가 내용에 접근했을때

                        if(isplntzrNm){ //isTitle이 true일 때 태그의 내용을 저장.
                            plntzrNm = parser.getText();
                            isplntzrNm = false;
                        }
                        if(ispostngplaceCodeNm){ //isTitle이 true일 때 태그의 내용을 저장.
                            postngplaceCodeNm = parser.getText();
                            ispostngplaceCodeNm = false;
                        }
                        if(isprpgtEraInfo){ //isTitle이 true일 때 태그의 내용을 저장.
                            prpgtEraInfo = parser.getText();
                            isprpgtEraInfo = false;
                        }
                        if(isprpgtmthCodeNm){ //isTitle이 true일 때 태그의 내용을 저장.
                            prpgtmthCodeNm = parser.getText();
                            isprpgtmthCodeNm = false;
                        }
                        if(issoilInfo){ //isTitle이 true일 때 태그의 내용을 저장.
                            soilInfo = parser.getText();
                            issoilInfo = false;
                        }
                        if(isspeclmanageInfo){ //isTitle이 true일 때 태그의 내용을 저장.
                            speclmanageInfo = parser.getText();
                            isspeclmanageInfo = false;
                        }
                        if(istoxctyInfo){ //isTitle이 true일 때 태그의 내용을 저장.
                            toxctyInfo = parser.getText();
                            istoxctyInfo = false;
                        }
                        if(iswinterLwetTpCodeNm){ //isTitle이 true일 때 태그의 내용을 저장.
                            winterLwetTpCodeNm = parser.getText();
                            iswinterLwetTpCodeNm = false;
                        }


                        System.out.println("Pasring진행중");
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("item")){
                            //이제 보여주는 창 작성하면 됨.
                            // String plntzrNm,postngplaceCodeNm,prpgtEraInfo,prpgtmthCodeNm,soilInfo, speclmanageInfo,toxctyInfo,winterLwetTpCodeNm= null;

                            t1.setText(plntzrNm);
                            t2.setText(postngplaceCodeNm);
                            t3.setText(prpgtEraInfo);
                            t4.setText(prpgtmthCodeNm);
                            t5.setText(soilInfo);
                            t6.setText(speclmanageInfo);
                            t7.setText(toxctyInfo);
                            t8.setText(winterLwetTpCodeNm);
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch(Exception e){
            System.out.println(e);

        }



        Button button =(Button)findViewById(R.id.btn);

        Spinner plant_name_SP = (Spinner)findViewById(R.id.spinner1);



        ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.plant_name, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        plant_name_SP.setAdapter(yearAdapter);


        int pt=0;
        String[] num_labelNames = {"2901", "12919", "12920", "12938", "12954", "12955", "12956", "12957", "12963", "12966", "12972", "12973", "12974", "12975", "12978", "12979", "12981", "12987", "12988", "12989", "12990", "12991", "12994", "12998", "13001", "13002", "13004", "13186", "13189", "13190", "13191", "13192", "13193", "13197", "13199", "13201", "13202", "13203", "13206", "13207", "13208", "13209", "13210", "13212", "13214", "13216", "13218", "13240", "13242", "13244", "13245", "13247", "13249", "13251", "13253", "13255", "13257", "13260", "13309", "13317", "13318", "13319", "13333", "13336", "13338", "13339", "13340", "14663", "14674", "14675", "14676", "14687", "14688", "14689", "14690", "14691", "14692", "14695", "14696", "14697", "14698", "14699", "14700", "14713", "14911", "14913", "14914", "14915", "14916", "14917", "14919", "14920", "15830", "15831", "15834", "15835", "16033", "16036", "16038", "16238", "16241", "16242", "16245", "16246", "16248", "16448", "16449", "17748", "17750", "17751", "18578", "18582", "18588", "18589", "18593", "18595", "18596", "18597", "18600", "18601", "18604", "18613", "18649", "18652", "18656", "18658", "18660", "18691", "18692", "18697", "19448", "19451", "19453", "19457", "19459", "19461", "19462", "19464", "19465", "19466", "19470", "19474", "19663", "19696", "19706", "19707", "19709", "19711", "19712", "19713", "19717", "19719"};
        String[] kor_labelNames = {"히포에스테스", "흰줄무늬달개비(트라데스칸티아)", "흰꽃나도사프란", "가울테리아", "개운죽", "골드크레스트 윌마", "후피향나무", "공작야자", "관음죽", "구문초", "구즈마니아", "호주매", "군자란", "호야 엑소티카", "협죽도", "헤테로파낙스 프라그란스 (해피트리)", "헤미오니티스(하트펀)", "해마리아", "글레코마", "치자나무", "금목서", "참쇠고비(섬쇠고비)", "금사철나무", "금식나무", "털머위", "테이블야자", "틸란드시아", "금전수", "필로덴드론 셀로움", "필로덴드론 선라이트", "필로덴드론 레몬라임", "필로덴드론 고엘디", "필레아 글라우카(타라", "피토니아 화이트스타", "기누라", "꽃베고니아", "나도풍란", "나한송", "남천", "네마탄투스", "네오레겔리아", "피라칸사", "녹영", "푸밀라고무나무", "포인세티아", "페페로미아 클루시폴리아", "뉴기니아봉선화", "페페로미아 오브투시폴리아", "팬더 고무나무", "팔손이나무", "팔레놉시스(호접란)", "파피오페딜럼", "필로덴드론 콩고", "피토니아 핑크스타", "팻츠헤데라", "클레마티스", "크립탄서스", "크로톤", "코르딜리네 레드에지", "커피나무", "켄챠야자", "칼랑코에", "대만고무나무", "덕구리난", "덴파레", "도깨비고비", "돈나무", "동백", "둥근잎 아랄리아", "듀란타", "드라세나 송오브자마이카", "드라세나 와네끼", "드라세나 드라코", "드라세나 마지나타", "드라세나 맛상게아나", "드라세나 산데리아나", "드라세나 산데리아나 세레스", "드라세나 수르쿨로사", "드라세나 자바", "드라세나 콤팩타", "드라세나 트리컬러 레인보우", "디지고데카", "디펜바키아 마리안느", "디펜바키아 트로픽스노우", "떡갈잎 고무나무", "러브체인", "칼라디움", "백정화", "백화등", "칼라데아 크로카타", "벤자민고무나무", "렉스베고니아", "루모라고사리", "벵갈고무나무", "줄리아 페페로미아", "병솔나무", "죽백나무", "종려방동사니", "마란타 류코뉴라", "마삭줄", "만데빌라", "좀마삭줄", "멕시코소철", "멜라니 고무나무", "제라니움", "목베고니아", "몬스테라", "인삼벤자민", "무늬마삭줄", "무늬벤자민고무나무", "익소라", "유카", "왜란", "온시디움", "무늬쉐플레라홍콩", "여우꼬리풀", "무늬알피니아", "무늬유카", "무늬털머위", "에크메아 파시아타", "무늬푸밀라고무나무", "부겐빌레아", "뮤렌베키아", "브룬펠시아", "바위취", "알로카시아 아마조니카", "안수리움", "아프리칸 바이올렛", "반딧불털머위", "비타툼접란", "산세베리아", "산호수", "상록넉줄고사리 후마타", "석창포", "아펠란드라", "아이비", "아왜나무", "아스파라거스 풀루모수스", "아레카야자", "아라우카리아", "아마릴리스", "옥살리스(사랑초)", "세이프릿지 야자", "셀라기넬라", "수박페페로미아", "시클라멘", "수박필레아", "수염 틸란드시아", "심비디움", "숙근이베리스", "스파티필룸", "시서스"};
        for (int i=0;i<kor_labelNames.length;i++)
        {
            //식물코드에 맞는 인덱스를 pt라고 한다.
            if(num_labelNames[i].equals(cntntsNO)){
                pt=i;
            }
        }
        plant_name_SP.setSelection(pt);

        System.out.println("pt!!!!!!!!!!!!!!!!!"+pt);

        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                //버튼 눌렀을 떄

                //Spinner에서 선택한 경우.
                Spinner plant_name_SP = (Spinner)findViewById(R.id.spinner1);
                String cntNO=plant_name_SP.getSelectedItem().toString();
                //식물이름에맞는 CODE보내야함.

                int pt=0;
                String[] num_labelNames = {"2901", "12919", "12920", "12938", "12954", "12955", "12956", "12957", "12963", "12966", "12972", "12973", "12974", "12975", "12978", "12979", "12981", "12987", "12988", "12989", "12990", "12991", "12994", "12998", "13001", "13002", "13004", "13186", "13189", "13190", "13191", "13192", "13193", "13197", "13199", "13201", "13202", "13203", "13206", "13207", "13208", "13209", "13210", "13212", "13214", "13216", "13218", "13240", "13242", "13244", "13245", "13247", "13249", "13251", "13253", "13255", "13257", "13260", "13309", "13317", "13318", "13319", "13333", "13336", "13338", "13339", "13340", "14663", "14674", "14675", "14676", "14687", "14688", "14689", "14690", "14691", "14692", "14695", "14696", "14697", "14698", "14699", "14700", "14713", "14911", "14913", "14914", "14915", "14916", "14917", "14919", "14920", "15830", "15831", "15834", "15835", "16033", "16036", "16038", "16238", "16241", "16242", "16245", "16246", "16248", "16448", "16449", "17748", "17750", "17751", "18578", "18582", "18588", "18589", "18593", "18595", "18596", "18597", "18600", "18601", "18604", "18613", "18649", "18652", "18656", "18658", "18660", "18691", "18692", "18697", "19448", "19451", "19453", "19457", "19459", "19461", "19462", "19464", "19465", "19466", "19470", "19474", "19663", "19696", "19706", "19707", "19709", "19711", "19712", "19713", "19717", "19719"};
                String[] kor_labelNames = {"히포에스테스", "흰줄무늬달개비(트라데스칸티아)", "흰꽃나도사프란", "가울테리아", "개운죽", "골드크레스트 윌마", "후피향나무", "공작야자", "관음죽", "구문초", "구즈마니아", "호주매", "군자란", "호야 엑소티카", "협죽도", "헤테로파낙스 프라그란스 (해피트리)", "헤미오니티스(하트펀)", "해마리아", "글레코마", "치자나무", "금목서", "참쇠고비(섬쇠고비)", "금사철나무", "금식나무", "털머위", "테이블야자", "틸란드시아", "금전수", "필로덴드론 셀로움", "필로덴드론 선라이트", "필로덴드론 레몬라임", "필로덴드론 고엘디", "필레아 글라우카(타라", "피토니아 화이트스타", "기누라", "꽃베고니아", "나도풍란", "나한송", "남천", "네마탄투스", "네오레겔리아", "피라칸사", "녹영", "푸밀라고무나무", "포인세티아", "페페로미아 클루시폴리아", "뉴기니아봉선화", "페페로미아 오브투시폴리아", "팬더 고무나무", "팔손이나무", "팔레놉시스(호접란)", "파피오페딜럼", "필로덴드론 콩고", "피토니아 핑크스타", "팻츠헤데라", "클레마티스", "크립탄서스", "크로톤", "코르딜리네 레드에지", "커피나무", "켄챠야자", "칼랑코에", "대만고무나무", "덕구리난", "덴파레", "도깨비고비", "돈나무", "동백", "둥근잎 아랄리아", "듀란타", "드라세나 송오브자마이카", "드라세나 와네끼", "드라세나 드라코", "드라세나 마지나타", "드라세나 맛상게아나", "드라세나 산데리아나", "드라세나 산데리아나 세레스", "드라세나 수르쿨로사", "드라세나 자바", "드라세나 콤팩타", "드라세나 트리컬러 레인보우", "디지고데카", "디펜바키아 마리안느", "디펜바키아 트로픽스노우", "떡갈잎 고무나무", "러브체인", "칼라디움", "백정화", "백화등", "칼라데아 크로카타", "벤자민고무나무", "렉스베고니아", "루모라고사리", "벵갈고무나무", "줄리아 페페로미아", "병솔나무", "죽백나무", "종려방동사니", "마란타 류코뉴라", "마삭줄", "만데빌라", "좀마삭줄", "멕시코소철", "멜라니 고무나무", "제라니움", "목베고니아", "몬스테라", "인삼벤자민", "무늬마삭줄", "무늬벤자민고무나무", "익소라", "유카", "왜란", "온시디움", "무늬쉐플레라홍콩", "여우꼬리풀", "무늬알피니아", "무늬유카", "무늬털머위", "에크메아 파시아타", "무늬푸밀라고무나무", "부겐빌레아", "뮤렌베키아", "브룬펠시아", "바위취", "알로카시아 아마조니카", "안수리움", "아프리칸 바이올렛", "반딧불털머위", "비타툼접란", "산세베리아", "산호수", "상록넉줄고사리 후마타", "석창포", "아펠란드라", "아이비", "아왜나무", "아스파라거스 풀루모수스", "아레카야자", "아라우카리아", "아마릴리스", "옥살리스(사랑초)", "세이프릿지 야자", "셀라기넬라", "수박페페로미아", "시클라멘", "수박필레아", "수염 틸란드시아", "심비디움", "숙근이베리스", "스파티필룸", "시서스"};
                for (int i=0;i<kor_labelNames.length;i++)
                {
                    if(kor_labelNames[i].equals(cntNO)){
                        pt=i;
                    }
                }


                //Spinner에서 얻은 식물이름에 맞는 코드를 찾아야한다.
                String cntNo2=num_labelNames[pt];

                Intent tmp = new Intent(getApplicationContext(), display_plant_info.class);

                tmp.putExtra("pltCode",cntNo2);
                startActivity(tmp);



            }

        });















    }




}

