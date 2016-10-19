extern malloc
extern println_int
extern print_int
extern println
extern print_pair
global merge_sort
merge_sort:
    push rbp
    mov rbp, rsp
    sub rsp, 8
    mov [rbp - 8], rdi
    sub rsp, 8
    mov [rbp - 16], rsi
    sub rsp, 8
    mov [rbp - 24], rdx
    sub rsp, 8
    mov [rbp - 32], rcx
    mov rax, [rbp - 32]
    push rax
    mov rax, [rbp - 24]
    pop r10
    mov r11, rax
    mov rax, r10
    sub rax, r11
    push rax
    mov rax, 1
    pop r10
    cmp r10, rax
    je L1
L0:
    xor rax, rax
    jmp L2
L1:
    mov rax, 1
L2:
    test rax, rax
    jz L3
    mov rax, 0
    add rsp, 32
    pop rbp
    ret
L3:
    sub rsp, 8
    lea rax, [rbp - 40]
    push rax
    mov rax, [rbp - 24]
    push rax
    mov rax, [rbp - 32]
    pop r10
    add rax, r10
    push rax
    mov rax, 2
    pop r10
    mov r11, rax
    mov rax, r10
    xor rdx, rdx
    div r11
    pop r10
    mov [r10], rax
    mov rax, [rbp - 32]
    push rax
    mov rax, [rbp - 24]
    pop r10
    mov r11, rax
    mov rax, r10
    sub rax, r11
    push rax
    mov rax, 2
    pop r10
    cmp r10, rax
    jg L5
L4:
    xor rax, rax
    jmp L6
L5:
    mov rax, 1
L6:
    test rax, rax
    jz L7
    mov rax, [rbp - 40]
    push rax
    mov rax, [rbp - 24]
    push rax
    mov rax, [rbp - 16]
    push rax
    mov rax, [rbp - 8]
    push rax
    pop rdi
    pop rsi
    pop rdx
    pop rcx
    call merge_sort
    mov rax, [rbp - 32]
    push rax
    mov rax, [rbp - 40]
    push rax
    mov rax, [rbp - 16]
    push rax
    mov rax, [rbp - 8]
    push rax
    pop rdi
    pop rsi
    pop rdx
    pop rcx
    call merge_sort
L7:
    sub rsp, 8
    lea rax, [rbp - 48]
    push rax
    mov rax, [rbp - 24]
    pop r10
    mov [r10], rax
    sub rsp, 8
    lea rax, [rbp - 56]
    push rax
    mov rax, [rbp - 40]
    pop r10
    mov [r10], rax
    sub rsp, 8
    lea rax, [rbp - 64]
    push rax
    mov rax, [rbp - 24]
    pop r10
    mov [r10], rax
L8:
    mov rax, [rbp - 48]
    push rax
    mov rax, [rbp - 40]
    pop r10
    cmp r10, rax
    jl L13
L12:
    xor rax, rax
    jmp L14
L13:
    mov rax, 1
L14:
    test rax, rax
    jz L10
    mov rax, [rbp - 56]
    push rax
    mov rax, [rbp - 32]
    pop r10
    cmp r10, rax
    jl L16
L15:
    xor rax, rax
    jmp L17
L16:
    mov rax, 1
L17:
    test rax, rax
    jz L10
    mov rax, 1
    jmp L11
L10:
    mov rax, 0
L11:
    test rax, rax
    jz L9
    sub rsp, 8
    lea rax, [rbp - 72]
    push rax
    mov rax, [rbp - 8]
    push rax
    mov rax, 8
    push rax
    mov rax, [rbp - 48]
    pop r10
    mul r10
    pop r10
    add rax, r10
    mov rax, [rax]
    pop r10
    mov [r10], rax
    sub rsp, 8
    lea rax, [rbp - 80]
    push rax
    mov rax, [rbp - 8]
    push rax
    mov rax, 8
    push rax
    mov rax, [rbp - 56]
    pop r10
    mul r10
    pop r10
    add rax, r10
    mov rax, [rax]
    pop r10
    mov [r10], rax
    mov rax, [rbp - 72]
    push rax
    mov rax, [rbp - 80]
    pop r10
    cmp r10, rax
    jl L19
L18:
    xor rax, rax
    jmp L20
L19:
    mov rax, 1
L20:
    test rax, rax
    jz L21
    mov rax, [rbp - 16]
    push rax
    mov rax, 8
    push rax
    mov rax, [rbp - 64]
    pop r10
    mul r10
    pop r10
    add rax, r10
    push rax
    mov rax, [rbp - 72]
    pop r10
    mov [r10], rax
    lea rax, [rbp - 48]
    push rax
    mov rax, [rbp - 48]
    push rax
    mov rax, 1
    pop r10
    add rax, r10
    pop r10
    mov [r10], rax
    lea rax, [rbp - 64]
    push rax
    mov rax, [rbp - 64]
    push rax
    mov rax, 1
    pop r10
    add rax, r10
    pop r10
    mov [r10], rax
L21:
    mov rax, [rbp - 80]
    push rax
    mov rax, [rbp - 72]
    pop r10
    cmp r10, rax
    jle L23
L22:
    xor rax, rax
    jmp L24
L23:
    mov rax, 1
L24:
    test rax, rax
    jz L25
    mov rax, [rbp - 16]
    push rax
    mov rax, 8
    push rax
    mov rax, [rbp - 64]
    pop r10
    mul r10
    pop r10
    add rax, r10
    push rax
    mov rax, [rbp - 80]
    pop r10
    mov [r10], rax
    lea rax, [rbp - 56]
    push rax
    mov rax, [rbp - 56]
    push rax
    mov rax, 1
    pop r10
    add rax, r10
    pop r10
    mov [r10], rax
    lea rax, [rbp - 64]
    push rax
    mov rax, [rbp - 64]
    push rax
    mov rax, 1
    pop r10
    add rax, r10
    pop r10
    mov [r10], rax
L25:
    add rsp, 16
    mov rax, 1
    jmp L8
L9:
L26:
    mov rax, [rbp - 48]
    push rax
    mov rax, [rbp - 40]
    pop r10
    cmp r10, rax
    jl L29
L28:
    xor rax, rax
    jmp L30
L29:
    mov rax, 1
L30:
    test rax, rax
    jz L27
    mov rax, [rbp - 16]
    push rax
    mov rax, 8
    push rax
    mov rax, [rbp - 64]
    pop r10
    mul r10
    pop r10
    add rax, r10
    push rax
    mov rax, [rbp - 8]
    push rax
    mov rax, 8
    push rax
    mov rax, [rbp - 48]
    pop r10
    mul r10
    pop r10
    add rax, r10
    mov rax, [rax]
    pop r10
    mov [r10], rax
    lea rax, [rbp - 64]
    push rax
    mov rax, [rbp - 64]
    push rax
    mov rax, 1
    pop r10
    add rax, r10
    pop r10
    mov [r10], rax
    lea rax, [rbp - 48]
    push rax
    mov rax, [rbp - 48]
    push rax
    mov rax, 1
    pop r10
    add rax, r10
    pop r10
    mov [r10], rax
    jmp L26
L27:
L31:
    mov rax, [rbp - 56]
    push rax
    mov rax, [rbp - 32]
    pop r10
    cmp r10, rax
    jl L34
L33:
    xor rax, rax
    jmp L35
L34:
    mov rax, 1
L35:
    test rax, rax
    jz L32
    mov rax, [rbp - 16]
    push rax
    mov rax, 8
    push rax
    mov rax, [rbp - 64]
    pop r10
    mul r10
    pop r10
    add rax, r10
    push rax
    mov rax, [rbp - 8]
    push rax
    mov rax, 8
    push rax
    mov rax, [rbp - 56]
    pop r10
    mul r10
    pop r10
    add rax, r10
    mov rax, [rax]
    pop r10
    mov [r10], rax
    lea rax, [rbp - 64]
    push rax
    mov rax, [rbp - 64]
    push rax
    mov rax, 1
    pop r10
    add rax, r10
    pop r10
    mov [r10], rax
    lea rax, [rbp - 56]
    push rax
    mov rax, [rbp - 56]
    push rax
    mov rax, 1
    pop r10
    add rax, r10
    pop r10
    mov [r10], rax
    jmp L31
L32:
    sub rsp, 8
    lea rax, [rbp - 72]
    push rax
    mov rax, [rbp - 24]
    pop r10
    mov [r10], rax
L36:
    mov rax, [rbp - 72]
    push rax
    mov rax, [rbp - 32]
    pop r10
    cmp r10, rax
    jl L39
L38:
    xor rax, rax
    jmp L40
L39:
    mov rax, 1
L40:
    test rax, rax
    jz L37
    mov rax, [rbp - 8]
    push rax
    mov rax, 8
    push rax
    mov rax, [rbp - 72]
    pop r10
    mul r10
    pop r10
    add rax, r10
    push rax
    mov rax, [rbp - 16]
    push rax
    mov rax, 8
    push rax
    mov rax, [rbp - 72]
    pop r10
    mul r10
    pop r10
    add rax, r10
    mov rax, [rax]
    pop r10
    mov [r10], rax
    lea rax, [rbp - 72]
    push rax
    mov rax, [rbp - 72]
    push rax
    mov rax, 1
    pop r10
    add rax, r10
    pop r10
    mov [r10], rax
    jmp L36
L37:
    add rsp, 8
    mov rax, 0
    add rsp, 64
    pop rbp
    ret
    add rsp, 64
    pop rbp
    ret
global f
f:
    push rbp
    mov rbp, rsp
    sub rsp, 8
    lea rax, [rbp - 8]
    push rax
    mov rax, 1000000
    pop r10
    mov [r10], rax
    sub rsp, 8
    lea rax, [rbp - 16]
    push rax
    mov rax, 8
    push rax
    mov rax, [rbp - 8]
    pop r10
    mul r10
    push rax
    pop rdi
    call malloc
    pop r10
    mov [r10], rax
    sub rsp, 8
    lea rax, [rbp - 24]
    push rax
    mov rax, 8
    push rax
    mov rax, [rbp - 8]
    pop r10
    mul r10
    push rax
    pop rdi
    call malloc
    pop r10
    mov [r10], rax
    sub rsp, 8
    lea rax, [rbp - 32]
    push rax
    mov rax, 0
    pop r10
    mov [r10], rax
L41:
    mov rax, [rbp - 32]
    push rax
    mov rax, [rbp - 8]
    pop r10
    cmp r10, rax
    jl L44
L43:
    xor rax, rax
    jmp L45
L44:
    mov rax, 1
L45:
    test rax, rax
    jz L42
    mov rax, [rbp - 16]
    push rax
    mov rax, 8
    push rax
    mov rax, [rbp - 32]
    pop r10
    mul r10
    pop r10
    add rax, r10
    push rax
    mov rax, [rbp - 8]
    push rax
    mov rax, [rbp - 32]
    pop r10
    mov r11, rax
    mov rax, r10
    sub rax, r11
    pop r10
    mov [r10], rax
    lea rax, [rbp - 32]
    push rax
    mov rax, [rbp - 32]
    push rax
    mov rax, 1
    pop r10
    add rax, r10
    pop r10
    mov [r10], rax
    jmp L41
L42:
    add rsp, 8
    mov rax, [rbp - 8]
    push rax
    mov rax, 0
    push rax
    mov rax, [rbp - 24]
    push rax
    mov rax, [rbp - 16]
    push rax
    pop rdi
    pop rsi
    pop rdx
    pop rcx
    call merge_sort
    add rsp, 24
    pop rbp
    ret

