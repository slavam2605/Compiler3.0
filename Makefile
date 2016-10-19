all: run

run: run.c sample.o
	gcc -m64 run.c  sample.o -o run

sample.o: sample.asm
	yasm -f elf64 -g dwarf2 sample.asm -o sample.o

clean:
	rm -f sample.o run
