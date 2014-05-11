package KMeansRecommender;

import java.io.File;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;


public class EvaluateRecommender {

	public static void main(String[] args) throws Exception {
		DataModel model = new FileDataModel(new File("data/movies.csv"));
		//RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
		RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();		
		int begin = 10;
		int end = 200;
		int step = 10;
		for(int i = begin; i <= end; i = i + step){
			RecommenderBuilder builder = new MyRecommenderBuilder(i, new LogLikelihoodSimilarity(model));
			double result = evaluator.evaluate(builder, null, model, 0.9, 1.0);
			System.out.println("k = " + i + ", result = " + result);
		}		
	}

}

class MyRecommenderBuilder implements  RecommenderBuilder {
	
	private ItemSimilarity similarity;
	private int k;
	private int maxiteration;
	
	public MyRecommenderBuilder(int k, ItemSimilarity similarity){
		this.k = k;
		this.similarity = similarity;
		maxiteration = 10000;
	}
	

	public Recommender buildRecommender(DataModel dataModel)
			throws TasteException {
		
		ItemSimilarity is = new KMeansItemSimilarity(dataModel, k, maxiteration, similarity);	
		//ItemSimilarity is = new LogLikelihoodSimilarity(dataModel);
		return new GenericItemBasedRecommender(dataModel, is);
	}
}