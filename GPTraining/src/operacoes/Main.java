package operacoes;
import static org.junit.Assert.assertEquals;
import gptraining.model.ArvoreExpressao;
import gptraining.model.CalculadorFitness;
import gptraining.model.Dataset;
import gptraining.model.No;

public class Main {
	
	public void main()
	{
		Dataset ds = criaDatasetLinear();
		No raiz = new No('+', new No("x"), new No(1.0));
		ArvoreExpressao arvore = new ArvoreExpressao(raiz);
		CalculadorFitness calculador = new CalculadorFitness();
		assertEquals(0.0, calculador.calcula(arvore, ds), 0.01);
	}
	
	private Dataset criaDatasetLinear()
	{
		Dataset ds = new Dataset();
		ds.adiciona(1, 2);
		ds.adiciona(2, 3);
		ds.adiciona(3, 4);
		ds.adiciona(4, 5);
		return ds;
	}

}
