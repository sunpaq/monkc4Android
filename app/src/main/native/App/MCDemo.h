//
// Created by Sun YuLi on 2016/12/10.
//

#ifndef SAPINDUS2_MCDEMO_H
#define SAPINDUS2_MCDEMO_H

#include "monkc.h"

class(MCDemo, MCObject,
    char name[256];
);

method(MCDemo, MCDemo*, initWithName, const char* name);
method(MCDemo, const char*, name, voida);


#endif //SAPINDUS2_MCDEMO_H
