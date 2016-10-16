#include <stdio.h>
#include <stdint.h>

extern int64_t f(int64_t x);

int main() {
    printf("%ld\n", f(20));
    return 0;
}
