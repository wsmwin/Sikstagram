package com.example.sikstagram;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import org.pytorch.Tensor;
import org.pytorch.Module;
import org.pytorch.IValue;
import org.pytorch.torchvision.TensorImageUtils;

import java.io.File;


public class Classifier {
    Module model;
    float[] mean = {0.485f, 0.456f, 0.406f};
    float[] std = {0.229f, 0.224f, 0.225f};

    String[] en_labelNames = {"Gaultheria procumbens", "Dracaena sanderiana Virens", "Cupressus macrocarpa Wilma", "Caryota mitis", "Begonia  spp.", "Rhapis  excelsa", "Pelargonium rosium", "Guzmania lingulata", "Clivia miniata", "Glecoma hederacea Variegata", "Osmanthus fragrans", "Euonymus japonica", "Aucuba japonica  var. variegata", "Zamioculcas zamiifolia", "Dracaena sanderiana", "Gynura aurantiaca", "Begonia semperflorens", "Aerides japonica", "Podocarpus macrophyllus", "Nandina domestica", "Nematanthus gregarius", "Neoregelia carolinae", "Senecio rowleyanus", "Impatiens hybrid New Guinea", "Ficus retusa", "Nephrolepis cordifolia Duffii", "Beaucarnea recurvata (Nolina tuberculata)", "Ficus elastica var. decora", "Dendrobium phalaenopsis", "Cyrtomium falcatum", "Pittosporum tobira", "Camellia japonica", "Polyscias balfouriana", "Duranta reptans", "Dracaena reflexa Song of Jamaica", "Dracaena deremensis Warneckii", "Dracaena reflexa Song of India", "Dracaena angustifolia Java", "Dracaena deremensis Virens Compacta", "Dracaena concinna Tricolor Rainbow", "Dracaena draco", "Dracaena marginata", "Dracaena fragrans var. massangeana", "Dracaena sanderiana", "Dracaena sanderiana Celes", "Dracaena surculosa (D. godseffiana)", "Dizygotheca elegantissima", "Dieffenbachia Marianne", "Dieffenbachia amoena Tropic snow", "Ficus lyrata", "Ceropegia woodii", "Begonia rex", "Rumohra adiantiformis", "Ruscus spp.", "Maranta leuconeura", "Trachelospermum asiaticum", "Rohdea japonica", "Mandevilla sanderi", "Zamia pumila", "Ficus elastica Robusta", "Begonia lucerna", "Monstera deliciosa", "Rhapis excelsa Variegata", "Trachelospermum asiaticum var.variegatum", "Ficus benjamina Variegata", "Ardisia pusilla Variegata", "Acorus gramineus var. variegatus", "Schefflera arboricola Hong Kong Variegata", "Alpinia zerumbet Variegata", "Yucca elephantipes Variegata", "Chlorophytum comosum var. variegatum", "Farfugium japonicum Argenteum", "Fatshedera lizei Variegata", "Ficus pumila Variegata", "Muehlenbeckia complexa", "Saxifraga stolonifera", "Platycerium  bifurcatum", "Farfugium japonicum", "Ardisia crenata", "Serissa foetida", "Trachelospermum asiaticum var. majus", "Ficus benjamina", "Ficus benjamina King", "Ficus benghalensis", "Callistemon spp.", "Nephrolepis exaltata Bostoniensis", "Pteris multifida", "Bougainvillea glabra", "Brunfelsia australis Bench", "Vriesea", "Chlorophytum bichetii", "Chlorophytum comosum var. vittatum", "Sansevieria trifasciata", "Sansevieria trifasciata Golden Hahnii", "Ardisia pusilla", "Decora elastica Decora Tricolor", "Humata tyermannii (Davallia griffithiana)", "Calanthe discolor spp.", "Acorus gramineus", "Senecio radicans", "Chamaedorea seifrizii Burret", "Selaginella", "Cycas revoluta", "Ficus elastica Sofia", "Soleirolia soleirolii", "Peperomia sandersii", "Pilea cadierei", "Tillandsia usneoides", "Iberis sempervirens", "Schefflera arboricola", "Epipremnum aureum", "Spathiphyllum wallisii", "Spathiphyllum Clevelandii", "Cissus antarctica", "Cyclamen persicum", "Cymbidium spp.", "Syngonium podophyllum", "Aglaonema commutatum", "Adenium obesum", "Adiantum raddianum", "Araucaria heterophylla", "Chrysalidocarpus lutescens", "Hippeastrum hybridum", "Asparagus pulmosus var. nanus", "Asplenium nidus", "Viburnum odoratissimum var. awabuki", "Hedera helix", "Aphelandra squarrosa", "Saintpaulia spp.", "Anthurium andraeanum", "Alocasia amazonica", "Alocasia cucullata", "Zebrina pendula", "Peperomia caperata", "Aechmea fasciata", "Begonia X hiemalis", "Acalypha reptans", "Aspidistra elatior", "Oxalis triangularis", "Oncidium spp.", "Ophiopogon japonicus", "Rhapis multifida", "Ficus umbellata", "Yucca", "Euonymus japonica Albomarginata", "Ixora chinensis", "Ficus microcarpa Ginseng", "Ardisia japonica", "Bletilla striata", "Rhoeo discolor", "Chlorophytum comosum", "Pelargonium hortorum", "Trachelospermum Atsuba Chirimen", "Cyperus alternifolius", "Rhapis humilis", "Podocarous nagi", "Peperomia puteolata", "Cyrtomium caryotideum var. koreanum", "Gardenia jasminoides", "Calathea makoyana", "Calathea insignis", "Calathea crocata", "Caladium bicolor (hortulantum) Vent.", "Kalanchoe blossfeldiana", "Coffea arabica", "Howea forsteriana", "Cordyline terminalis Rededge", "Codiaeum variegatum", "Codiaeum variegatum var. pictum", "Cryptanthus spp.", "Clematis florida", "Tradescantia sillamontana", "Farfugium japonicum", "Chamaedorea elegans", "Tolmiea menziesii", "Tillandsia cyanea", "Pachira aquatica", "Paphiopedilum hybrids", "Phaelenopsis spp.", "Fatsia japonica", "Ficus panda", "Fatshedera lizei", "Peperomia obtusifolia", "Peperomia clusiifolia", "Peperomia puteolata", "Euphorbia pulcherrima", "Polyscias balfouriana Marginata", "Ficus pumila", "Pteris cretica", "Pyracantha coccinea", "Fittonia  verschaffeltii Pink Star", "Fittonia  verschaffelti White Star", "Chlorophytum comosum Picturatum", "Pilea grauca", "Philodendron Sunlight", "Philodendron Xanadu", "Philodendron goeldii", "Philodendron Lemon Lime", "Philodendron selloum", "Philodendron oxycardium", "Philodendron Congo", "Haemaria discolor var. dawsoniana", "Dracaena fragrans", "Hemionitis arifolia", "Heteropanax fragrans", "Nerium indicum", "Epipremnum aureum Lime", "Hoya carnosa", "Hoya carnosa Exotica Hort", "Leptospermum scoparium", "Peperomia angula", "Trachelospermum asiaticum var. variegatum", "Dracaena sanderiana", "Ternstroemia japonica", "Zephyranthes candida", "Tradescantia albiflora Kunth Albovittata", "Hypoestes phyllostachya (H. sanguinnolenta)"};
    String[] kor_labelNames = {""}; // TODO: Add Kor Names

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

    public String[] predict(Bitmap bitmap, String language) {
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
        if(language == "en") {
            String[] results = {en_labelNames[classIndexes[0]], en_labelNames[classIndexes[1]], en_labelNames[classIndexes[2]], en_labelNames[classIndexes[3]], en_labelNames[classIndexes[4]]};
            return results;
        }else if(language == "kor"){
            String[] results = {kor_labelNames[classIndexes[0]], kor_labelNames[classIndexes[1]], kor_labelNames[classIndexes[2]], kor_labelNames[classIndexes[3]], kor_labelNames[classIndexes[4]]};
            return results;
        }else{
            throw new IllegalArgumentException(language);
        }
    }
}