#include <stdint.h>

int64_t* malloc(int64_t);
void free(int64_t*);

int64_t* merge_sortc(int64_t* a, int64_t* b, int64_t l, int64_t r) {
    if (r - l == 1) {
        return 0;
    }
    int64_t m = (l + r) / 2;
    if (r - l > 2)  {
        merge_sortc(a, b, l, m);
        merge_sortc(a, b, m, r);
    }
    int64_t c1 = l;
    int64_t c2 = m;
    int64_t p = l;
    for (; c1 < m && c2 < r; 1) {
        int64_t v1 = *(a + c1);
        int64_t v2 = *(a + c2);
        if (v1 < v2) {
            *(b + p) = v1;
            c1 = c1 + 1;
            p = p + 1;
        }
        if (v2 <= v1) {
            *(b + p) = v2;
            c2 = c2 + 1;
            p = p + 1;
        }
    }
    for (; c1 < m; c1 = c1 + 1) {
        *(b + p) = *(a + c1);
        p = p + 1;
    }
    for (; c2 < r; c2 = c2 + 1) {
        *(b + p) = *(a + c2);
        p = p + 1;
    }
    for (int64_t i = l; i < r; i = i + 1) {
        *(a + i) = *(b + i);
    }
    return 0;
}

int64_t* fc(int64_t* a, int64_t N) {
    int64_t* b = malloc(8 * N);
    merge_sortc(a, b, 0, N);
    free(b);
    return a;
}
