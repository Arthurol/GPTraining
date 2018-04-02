package gptraining.model;

public class CalculadorFitness
{
	public double pred;
	
	/*
	public double calcula(ArvoreExpressao arvore, Dataset dataset)
	{
		double distancia = 0.0;
		
		for (Dataset.Entrada entrada : dataset.getEntradas())
		{
			double yConhecido = entrada.getY();
			double yCalculado;
			try 
			{
				yCalculado = arvore.resolverExpressao(entrada.getX());
			} catch (Exception e) {
				return -1;
			}
			distancia += Math.pow(yConhecido - yCalculado, 2);
		}
		
		return Math.sqrt(distancia);
	}
	*/
	
	public double calcula (ArvoreExpressao arvore, Dataset dataset)
	{
		pred = 0.0;
		double mmre = 0.0;
		double somatorioMre = 0.0;
		int tamanhoDataset = 0;
		
		for (Dataset.Entrada entrada : dataset.getEntradas())
		{
			tamanhoDataset ++;
			double yConhecido = entrada.getY();
			double yCalculado;
			try 
			{
				yCalculado = arvore.resolverExpressao(entrada.getX());
			} catch (Exception e) {
				return -1;
			}
			somatorioMre += Math.abs((yConhecido - yCalculado) / yConhecido);
			
			if ((yCalculado < (yConhecido * 1.25)) && (yCalculado > (yConhecido * 0.75)))
				pred++;
		}
		
		mmre = somatorioMre / tamanhoDataset;
		pred = pred / tamanhoDataset;
		arvore.setPred(pred);
		return mmre;
		
	}
	
}