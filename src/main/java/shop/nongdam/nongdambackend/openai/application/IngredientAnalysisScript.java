package shop.nongdam.nongdambackend.openai.application;

public class IngredientAnalysisScript {
    public static String script(String region, String ingredientName){
        return  region + " 지역의 " + ingredientName + "의 파종과 수확 시기를 검색하여 파악해줘\n" +
                "그 시기동안의 그 지역의 날씨 데이터를 검색해서 파악해줘\n" +
                "이 데이터들을 바탕으로\n" +
                "지금 구매하는" + ingredientName + "은/는 수확시기로부터 어느정도 지났는지,\n" +
                "수확 시기와 파종 시기 사이의 날씨 정보로부터 품질이 어떨지,\n" +
                "가격은 이전에 비해 올랐을지, 떨어졌을지 3줄 요약, 200자로 정리해줘. 한 줄씩 단락으로 끊어줘";
    }
}
