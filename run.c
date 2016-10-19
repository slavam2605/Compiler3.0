#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>

extern int64_t* f();

FILE* file;

void println_int(int64_t x) {
    fprintf(file, "%ld\n", x);
}

void print_int(int64_t x) {
    fprintf(file, "%ld ", x);
}

void println() {
    fprintf(file, "\n");
}

void print_pair(int64_t x, int64_t y) {
    printf("(%ld, %ld) ", x, y);
    fflush(stdout);
}

int main() {
    file = fopen("log.txt", "w");
    f();
    fclose(file);
    return 0;
}
