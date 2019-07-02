import static java.lang.String.format;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class GatheringFood {

	static int n;
	static Coordenada[] grafo;
	static Coordenada[] letras;
	static int qtdeLetras;

	public static void main(String args[]) {
		Scanner scanner = new Scanner(System.in);

		int cases = 0;
		n = scanner.nextInt();
		while (n != 0) {
			scanner.nextLine();

			grafo = new Coordenada[n * n];
			letras = new Coordenada[26];
			qtdeLetras = 0;

			for (int x = 0; x < n; x++) {
				String linha = scanner.nextLine();

				for (int y = 0; y < n; y++) {
					char valor = linha.charAt(y);
					Coordenada coordenada = new Coordenada(x, y, valor);
					grafo[coordenada2Indice(x, y)] = coordenada;

					if (Character.isLetter(valor)) {
						letras[valor - 'A'] = coordenada;
						qtdeLetras = Integer.max(qtdeLetras, valor - 'A');
					}
				}
			}
			System.out.printf("Case %d: ", ++cases);
			bfs();
			n = scanner.nextInt();
		}

		scanner.close();
	}

	public static void bfs() {
		int totalDistancia = 0, totalDeCaminhos = 1;

		for (int i = 0; i < qtdeLetras; i++) {
			Coordenada coordenadaLetra = letras[i];
			coordenadaLetra.valor = '.';

			Queue<Coordenada> fila = new LinkedList<Coordenada>();
			fila.add(coordenadaLetra);

			Integer distancias[] = new Integer[n * n];
			int[] caminhos = new int[n * n];

			distancias[coordenada2Indice(coordenadaLetra)] = 0;
			caminhos[coordenada2Indice(coordenadaLetra)] = 1;

			char proximaLetra = (char) ('A' + i + 1);

			do {
				Coordenada coordenada = fila.remove();
				int indiceCoordenada = coordenada2Indice(coordenada);

				for (Coordenada ligado : coordenadasLigadas(coordenada)) {

					if (ligado.valor == '#' || (Character.isLetter(ligado.valor) && ligado.valor != proximaLetra))
						continue;

					int indiceLigado = coordenada2Indice(ligado);

					if (distancias[indiceLigado] == null) {
						distancias[indiceLigado] = distancias[indiceCoordenada] + 1;
						fila.add(ligado);
					}

					if (distancias[indiceLigado] == distancias[indiceCoordenada] + 1)
						caminhos[indiceLigado] += caminhos[indiceCoordenada];
				}

			} while (!fila.isEmpty());

			int indiceProximaLetra = coordenada2Indice(letras[i + 1]);

			if (distancias[indiceProximaLetra] == null) {
				System.out.println("Impossible");
				return;
			}

			totalDistancia += distancias[indiceProximaLetra];

			totalDeCaminhos *= caminhos[indiceProximaLetra];
			totalDeCaminhos %= 20437;
		}

		System.out.printf("%d %d\n", totalDistancia, totalDeCaminhos);
	}

	private static int coordenada2Indice(int x, int y) {
		return x * n + y;
	}

	private static int coordenada2Indice(Coordenada coordenada) {
		return coordenada2Indice(coordenada.x, coordenada.y);
	}

	private static List<Coordenada> coordenadasLigadas(Coordenada coordenada) {
		List<Coordenada> ligados = new ArrayList<Coordenada>();

		adicionaCoordenadas(ligados, coordenada.x, coordenada.y + 1);
		adicionaCoordenadas(ligados, coordenada.x, coordenada.y - 1);
		adicionaCoordenadas(ligados, coordenada.x + 1, coordenada.y);
		adicionaCoordenadas(ligados, coordenada.x - 1, coordenada.y);

		return ligados;
	}

	private static void adicionaCoordenadas(List<Coordenada> ligados, int x, int y) {
		if (x < 0 || x >= n)
			return;

		if (y < 0 || y >= n)
			return;

		ligados.add(grafo[coordenada2Indice(x, y)]);
	}

	public static class Coordenada {
		int x;
		int y;
		char valor;

		public Coordenada(int x, int y, char valor) {
			this.x = x;
			this.y = y;
			this.valor = valor;
		}

		@Override
		public String toString() {
			return format("%d x %d -> %c", x, y, valor);
		}
	}

}