#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <time.h>

extern int64_t* f(int64_t*, int64_t);

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

#define N 10000000

int64_t a[N];
int64_t b[N];

int comp(const void* a, const void* b) {
    int va = *(int*)a;
    int vb = *(int*)b;
    if (va > vb) return 1;
    if (va < vb) return -1;
    return 0;
}

int main(int argc, char* argv[]) {
    for (int i = 0; i < N; i++) 
        a[i] = b[i] = rand();
    clock_t start = clock();
    qsort(a, N, sizeof(int64_t), comp);
    clock_t end = clock();
    printf("qsort done in %f sec\n", (float)(end - start) / CLOCKS_PER_SEC);
    start = clock();
    f(b, N);
    end = clock();
    printf("f done in %f sec\n", (float)(end - start) / CLOCKS_PER_SEC);
    for (int i = 0; i < N; i++) {
        if (a[i] != b[i])
            printf("Mismatch a-b at position %d: %ld != %ld\n", i, a[i], b[i]);
    }
    
    return 0;
}
