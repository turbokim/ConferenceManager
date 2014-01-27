#define IP_0 0
#define IP_1 1

#define LOW_BYTE 0
#define HI_BYTE 1


//This is just what I had in mind...What do you think?
void cpu_storeByte(CpuPtr the_cpu) {
    ushort address = registerFile_getRegValue(the_cpu->registerFile, the_cpu->Reg2, &the_cpu ->error);
    uchar low_byte = registerFile_getByteFrom(the_cpu->registerFile, the_cpu->Reg1, LOW_BYTE, &the_cpu->error);

    switch (the_cpu ->modifier) {
        case 0://Register mode // M[$Ra] <- ($Rs(lob))
            the_cpu->error |= StoreByteTo(the_cpu->mainMemory, address, low_byte);
            break;
        case 1://base_relative // M[BP + $Ra] <- ($Rs(lob))
            the_cpu->error |= StoreByteTo(the_cpu->mainMemory, getBaseReg(the_cpu) + address, low_byte);
            break;
        case 2://index mode  // M[IP-1] <- ($Rs(lob)); IP-1 <- IP-1 + 1
            the_cpu->error |= StoreByteTo(the_cpu->mainMemory, getIndexReg(the_cpu, IP_1), low_byte);
            the_cpu->error |= registerFile_putRegValue(the_cpu->registerFile, $RD, getIndexReg(the_cpu, IP_1) + 1);
            break;
        case 3://indirect mode M[M[$Ra]] <- ($Rs(lob))
            the_cpu->error |= StoreByteTo(the_cpu->mainMemory, getIndirectMemAddress(the_cpu, LOW_BYTE), low_byte);
            break;
    }







//helpers for the different addressing modes
ushort getBaseReg(CpuPtr the_cpu) {//base pointer for base-relative mode
    return registerFile_getRegValue(the_cpu->registerFile, $RE, &the_cpu->error);
}

ushort getIndexReg(CpuPtr the_cpu, int which) {
    ushort r;
    if (which == 0) {
        r = 0XC; //$RC  IP-0    
    } else {
        r = 0XD; //$RD  IP-1      
    }

    return registerFile_getRegValue(the_cpu->registerFile, r, &the_cpu->error);
}

ushort getIndirectMemAddress(CpuPtr the_cpu, int which) {
    ushort address = registerFile_getRegValue(the_cpu->registerFile, the_cpu->Reg2, &the_cpu ->error);
    ushort indr_address;
    if (which == 0) {
        //low byte
        indr_address = FetchWordFrom(the_cpu->mainMemory, address + 1, &the_cpu->error);

    } else {
        //high byte
        indr_address = FetchWordFrom(the_cpu->mainMemory, address, &the_cpu->error);
    }
    return indr_address;
}