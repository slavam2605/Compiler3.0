#include <stdio.h>
#include <stdint.h>

extern int8_t f();

int main() {
    printf("%d\n", f());
    return 0;
}
