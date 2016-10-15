#include <stdio.h>
#include <stdint.h>

extern int64_t f();

int main() {
    printf("%ld\n", f());
    return 0;
}
