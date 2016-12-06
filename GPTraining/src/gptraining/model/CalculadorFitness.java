package gptraining.model;

public class CalculadorFitness
{
	public double calcula(ArvoreExpressao arvore, Dataset dataset)
	{
		double distancia = 0.0;
		
		for (Dataset.Entrada entrada : dataset.getEntradas())
		{
			double yConhecido = entrada.getY();
			double yCalculado = arvore.resolverExpressao(entrada.getX());
			distancia += Math.pow(yConhecido - yCalculado, 2);
		}
		
		return Math.sqrt(distancia);
	}
}