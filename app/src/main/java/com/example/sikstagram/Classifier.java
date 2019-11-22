package com.example.sikstagram;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import org.pytorch.IValue;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;

import java.io.File;


public class Classifier {
    Module model;
    float[] mean = {0.485f, 0.456f, 0.406f};
    float[] std = {0.229f, 0.224f, 0.225f};

    String[] num_labelNames = {"2901", "12919", "12920", "12938", "12954", "12955", "12956", "12957", "12963", "12966", "12972", "12973", "12974", "12975", "12978", "12979", "12981", "12987", "12988", "12989", "12990", "12991", "12994", "12998", "13001", "13002", "13004", "13186", "13189", "13190", "13191", "13192", "13193", "13197", "13199", "13201", "13202", "13203", "13206", "13207", "13208", "13209", "13210", "13212", "13214", "13216", "13218", "13240", "13242", "13244", "13245", "13247", "13249", "13251", "13253", "13255", "13257", "13260", "13309", "13317", "13318", "13319", "13333", "13336", "13338", "13339", "13340", "14663", "14674", "14675", "14676", "14687", "14688", "14689", "14690", "14691", "14692", "14695", "14696", "14697", "14698", "14699", "14700", "14713", "14911", "14913", "14914", "14915", "14916", "14917", "14919", "14920", "15830", "15831", "15834", "15835", "16033", "16036", "16038", "16238", "16241", "16242", "16245", "16246", "16248", "16448", "16449", "17748", "17750", "17751", "18578", "18582", "18588", "18589", "18593", "18595", "18596", "18597", "18600", "18601", "18604", "18613", "18649", "18652", "18656", "18658", "18660", "18691", "18692", "18697", "19448", "19451", "19453", "19457", "19459", "19461", "19462", "19464", "19465", "19466", "19470", "19474", "19663", "19696", "19706", "19707", "19709", "19711", "19712", "19713", "19717", "19719"};
    String[] en_labelNames = {"Hypoestes phyllostachya (H. sanguinnolenta)", "Tradescantia albiflora Kunth Albovittata", "Zephyranthes candida", "Gaultheria procumbens", "Dracaena sanderiana Virens", "Cupressus macrocarpa Wilma", "Ternstroemia japonica", "Caryota mitis", "Rhapis  excelsa", "Pelargonium rosium", "Guzmania lingulata", "Leptospermum scoparium", "Clivia miniata", "Hoya carnosa Exotica Hort", "Nerium indicum", "Heteropanax fragrans", "Hemionitis arifolia", "Haemaria discolor var. dawsoniana", "Glecoma hederacea Variegata", "Gardenia jasminoides", "Osmanthus fragrans", "Cyrtomium caryotideum var. koreanum", "Euonymus japonica", "Aucuba japonica  var. variegata", "Farfugium japonicum", "Chamaedorea elegans", "Tillandsia cyanea", "Zamioculcas zamiifolia", "Philodendron selloum", "Philodendron Sunlight", "Philodendron Lemon Lime", "Philodendron goeldii", "Pilea grauca", "Fittonia  verschaffelti White Star", "Gynura aurantiaca", "Begonia semperflorens", "Aerides japonica", "Podocarpus macrophyllus", "Nandina domestica", "Nematanthus gregarius", "Neoregelia carolinae", "Pyracantha coccinea", "Senecio rowleyanus", "Ficus pumila", "Euphorbia pulcherrima", "Peperomia clusiifolia", "Impatiens hybrid New Guinea", "Peperomia obtusifolia", "Ficus panda", "Fatsia japonica", "Phaelenopsis spp.", "Paphiopedilum hybrids", "Philodendron Congo", "Fittonia  verschaffeltii Pink Star", "Fatshedera lizei", "Clematis florida", "Cryptanthus spp.", "Codiaeum variegatum", "Cordyline terminalis Rededge", "Coffea arabica", "Howea forsteriana", "Kalanchoe blossfeldiana", "Ficus retusa", "Beaucarnea recurvata (Nolina tuberculata)", "Dendrobium phalaenopsis", "Cyrtomium falcatum", "Pittosporum tobira", "Camellia japonica", "Polyscias balfouriana", "Duranta reptans", "Dracaena reflexa Song of Jamaica", "Dracaena deremensis Warneckii", "Dracaena draco", "Dracaena marginata", "Dracaena fragrans var. massangeana", "Dracaena sanderiana", "Dracaena sanderiana Celes", "Dracaena surculosa (D. godseffiana)", "Dracaena angustifolia Java", "Dracaena deremensis Virens Compacta", "Dracaena concinna Tricolor Rainbow", "Dizygotheca elegantissima", "Dieffenbachia Marianne", "Dieffenbachia amoena Tropic snow", "Ficus lyrata", "Ceropegia woodii", "Caladium bicolor (hortulantum) Vent.", "Serissa foetida", "Trachelospermum asiaticum var. majus", "Calathea crocata", "Ficus benjamina", "Begonia rex", "Rumohra adiantiformis", "Ficus benghalensis", "Peperomia puteolata", "Callistemon spp.", "Podocarous nagi", "Cyperus alternifolius", "Maranta leuconeura", "Trachelospermum asiaticum", "Mandevilla sanderi", "Trachelospermum Atsuba Chirimen", "Zamia pumila", "Ficus elastica Robusta", "Pelargonium hortorum", "Begonia lucerna", "Monstera deliciosa", "Ficus microcarpa Ginseng", "Trachelospermum asiaticum var.variegatum", "Ficus benjamina Variegata", "Ixora chinensis", "Yucca", "Ophiopogon japonicus", "Oncidium spp.", "Schefflera arboricola Hong Kong Variegata", "Acalypha reptans", "Alpinia zerumbet Variegata", "Yucca elephantipes Variegata", "Farfugium japonicum Argenteum", "Aechmea fasciata", "Ficus pumila Variegata", "Bougainvillea glabra", "Muehlenbeckia complexa", "Brunfelsia australis Bench", "Saxifraga stolonifera", "Alocasia amazonica", "Anthurium andraeanum", "Saintpaulia spp.", "Farfugium japonicum", "Chlorophytum comosum var. vittatum", "Sansevieria trifasciata", "Ardisia pusilla", "Humata tyermannii (Davallia griffithiana)", "Acorus gramineus", "Aphelandra squarrosa", "Hedera helix", "Viburnum odoratissimum var. awabuki", "Asparagus pulmosus var. nanus", "Chrysalidocarpus lutescens", "Araucaria heterophylla", "Hippeastrum hybridum", "Oxalis triangularis", "Chamaedorea seifrizii Burret", "Selaginella", "Peperomia sandersii", "Cyclamen persicum", "Pilea cadierei", "Tillandsia usneoides", "Cymbidium spp.", "Iberis sempervirens", "Spathiphyllum wallisii", "Cissus antarctica"};
    String[] kor_labelNames = {"히포에스테스", "흰줄무늬달개비(트라데스칸티아)", "흰꽃나도사프란", "가울테리아", "개운죽", "골드크레스트 윌마", "후피향나무", "공작야자", "관음죽", "구문초", "구즈마니아", "호주매", "군자란", "호야 엑소티카", "협죽도", "헤테로파낙스 프라그란스 (해피트리)", "헤미오니티스(하트펀)", "해마리아", "글레코마", "치자나무", "금목서", "참쇠고비(섬쇠고비)", "금사철나무", "금식나무", "털머위", "테이블야자", "틸란드시아", "금전수", "필로덴드론 셀로움", "필로덴드론 선라이트", "필로덴드론 레몬라임", "필로덴드론 고엘디", "필레아 글라우카(타라", "피토니아 화이트스타", "기누라", "꽃베고니아", "나도풍란", "나한송", "남천", "네마탄투스", "네오레겔리아", "피라칸사", "녹영", "푸밀라고무나무", "포인세티아", "페페로미아 클루시폴리아", "뉴기니아봉선화", "페페로미아 오브투시폴리아", "팬더 고무나무", "팔손이나무", "팔레놉시스(호접란)", "파피오페딜럼", "필로덴드론 콩고", "피토니아 핑크스타", "팻츠헤데라", "클레마티스", "크립탄서스", "크로톤", "코르딜리네 레드에지", "커피나무", "켄챠야자", "칼랑코에", "대만고무나무", "덕구리난", "덴파레", "도깨비고비", "돈나무", "동백", "둥근잎 아랄리아", "듀란타", "드라세나 송오브자마이카", "드라세나 와네끼", "드라세나 드라코", "드라세나 마지나타", "드라세나 맛상게아나", "드라세나 산데리아나", "드라세나 산데리아나 세레스", "드라세나 수르쿨로사", "드라세나 자바", "드라세나 콤팩타", "드라세나 트리컬러 레인보우", "디지고데카", "디펜바키아 마리안느", "디펜바키아 트로픽스노우", "떡갈잎 고무나무", "러브체인", "칼라디움", "백정화", "백화등", "칼라데아 크로카타", "벤자민고무나무", "렉스베고니아", "루모라고사리", "벵갈고무나무", "줄리아 페페로미아", "병솔나무", "죽백나무", "종려방동사니", "마란타 류코뉴라", "마삭줄", "만데빌라", "좀마삭줄", "멕시코소철", "멜라니 고무나무", "제라니움", "목베고니아", "몬스테라", "인삼벤자민", "무늬마삭줄", "무늬벤자민고무나무", "익소라", "유카", "왜란", "온시디움", "무늬쉐플레라홍콩", "여우꼬리풀", "무늬알피니아", "무늬유카", "무늬털머위", "에크메아 파시아타", "무늬푸밀라고무나무", "부겐빌레아", "뮤렌베키아", "브룬펠시아", "바위취", "알로카시아 아마조니카", "안수리움", "아프리칸 바이올렛", "반딧불털머위", "비타툼접란", "산세베리아", "산호수", "상록넉줄고사리 후마타", "석창포", "아펠란드라", "아이비", "아왜나무", "아스파라거스 풀루모수스", "아레카야자", "아라우카리아", "아마릴리스", "옥살리스(사랑초)", "세이프릿지 야자", "셀라기넬라", "수박페페로미아", "시클라멘", "수박필레아", "수염 틸란드시아", "심비디움", "숙근이베리스", "스파티필룸", "시서스"};

    public Classifier(String modelPath) {
        File file = new File(modelPath);
        model = Module.load(modelPath);
    }

    public void setMeanAndStd(float[] mean, float[] std) {
        this.mean = mean;
        this.std = std;
    }

    public Tensor preprocess(Bitmap bitmap, int size) {
        bitmap = Bitmap.createScaledBitmap(bitmap, size, size, false);
        return TensorImageUtils.bitmapToFloat32Tensor(bitmap, this.mean, this.std);
    }

    public int[] argMax(float[] inputs) {
        int[] topIndexes = {-1, -1, -1, -1, -1};
        float[] topValues = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f};

        for (int i = 0; i < inputs.length; i++)
            for (int topArrIndex = 0; topArrIndex < 5; topArrIndex++)
                if (inputs[i] > topValues[topArrIndex]) {
                    for (int tempIndex = 4; tempIndex > topArrIndex; tempIndex--) {
                        topIndexes[tempIndex] = topIndexes[tempIndex - 1];
                        topValues[tempIndex] = topValues[tempIndex - 1];
                    }
                    topIndexes[topArrIndex] = i;
                    topValues[topArrIndex] = inputs[i];
                    break;
                }

        return topIndexes;
    }

    public String[] predict(Bitmap bitmap, String result_type) {
        Matrix matrix = new Matrix();
        matrix.postScale(0.5f, 0.5f);
        Bitmap croppedBitmap = Bitmap.createBitmap(bitmap,
                (bitmap.getWidth() - 224) / 2, (bitmap.getHeight() - 224) / 2,
                224, 224, matrix, true);
        Tensor tensor = preprocess(croppedBitmap, 224);
        IValue inputs = IValue.from(tensor);
        Tensor outputs = model.forward(inputs).toTensor();
        float[] scores = outputs.getDataAsFloatArray();
        int[] classIndexes = argMax(scores);

        if (result_type == "en") {
            String[] results = {en_labelNames[classIndexes[0]], en_labelNames[classIndexes[1]], en_labelNames[classIndexes[2]], en_labelNames[classIndexes[3]], en_labelNames[classIndexes[4]]};
            return results;
        } else if (result_type == "kor") {
            String[] results = {kor_labelNames[classIndexes[0]], kor_labelNames[classIndexes[1]], kor_labelNames[classIndexes[2]], kor_labelNames[classIndexes[3]], kor_labelNames[classIndexes[4]]};
            return results;
        } else if (result_type == "label") {
            String[] results = {num_labelNames[classIndexes[0]], num_labelNames[classIndexes[1]], num_labelNames[classIndexes[2]], num_labelNames[classIndexes[3]], num_labelNames[classIndexes[4]]};
            return results;
        } else {
            throw new IllegalArgumentException(result_type);
        }
    }
}