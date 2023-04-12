[Static Application Security Testing](https://github.com/piekaa/ing-tesla/actions/workflows/codeql.yml)

![CodeQL](https://github.com/piekaa/ing-tesla/actions/workflows/codeql.yml/badge.svg)

Treści zadań po polsku, to moje mini ADRy tak samo.

### Wstęp
Chcąc być ekologicznym, musimy się czasem trochę poświęcić.
Np. ładując samochód kilkadziesiąt minut, zamiast zatankować w kilka,
mieć prąd tylko kiedy wiatr wieje lub świeci słońce. Tak samo
jeśli chcemy, aby kod działał ekologicznie, trzeba się liczyć
z pewnymi niedogodnościami, takimi jak np. brak validacji, czy
też konieczność jednolitego formatowanie żądania.

#### Transakcje
Jeżeli chodzi o sam algorytm, za wiele raczej się tutaj zrobić nie da, sortowanie więc 
złożoność liniowo-logarytmiczna. Natomiast w związku z wielkością danych wejściowych
powstaje pole do optymalizacji czasu parsowania JSONa na obiekt i minimalizacji użycia
GC. Stąd ta dziwna mapa, klasa Decimal, czy analiza formatu JSONa.

#### Gra Online
Tutaj maksymalny rozmiar danych wejściowych jest znacznie mniejszy, więc parsowanie
JSONa czy optymalizacja pamięci nie jest kluczowa. Za to można było popracować nad algorytmem.

#### ATM
Nie jest powiedziane jaki jest maksymalny rozmiar danych wejściowych, więc nie ma mowy np.
o przygotowaniu tablicy/kolekcji o jakimś rozmiarze i jej ponownym użyciu. 
Wąskim gardłem algorytmu jest sortowanie, więc też nie widzę pola do popisu.

Pozdrawiam ;)