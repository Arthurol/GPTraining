package gptraining.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe que representa um dataset para o problema de geração de expressões
 * 
 * @author marciobarros
 */
public class Dataset
{
	private List<Entrada> entradas;
	private @Getter @Setter String nome;

	/**
	 * Classe que representa uma entrada do dataset
	 */
	public @Data class Entrada
	{
		private double x;
		private double y;
	}
	
	/**
	 * Inicializa o dataset
	 */
	public Dataset()
	{
		this.entradas = new ArrayList<Entrada>();
	}
	
	/**
	 * Adiciona uma entrada no dataset
	 */
	public void adiciona(double x, double y)
	{
		Entrada entrada = new Entrada();
		entrada.setX(x);
		entrada.setY(y);
		entradas.add(entrada);
	}
	
	/**
	 * Retorna todas as entradas do dataset
	 */
	public Iterable<Entrada> getEntradas()
	{
		return entradas;
	}
}