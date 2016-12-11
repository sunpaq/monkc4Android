//
// Created by Sun YuLi on 2016/12/10.
//

#include "MCDemo.h"

oninit(MCDemo)
{
    if(init(MCObject)) {

    } else {
        return null;
    }
}

method(MCDemo, MCDemo*, initWithName, const char* name)
{
    strcpy(var(name), name);
    return obj;
}

method(MCDemo, const char*, name, voida)
{
    return var(name);
}

onload(MCDemo)
{
    if(load(MCObject)) {
        binding(MCDemo, MCDemo*, initWithName, const char* name);
        binding(MCDemo, const char*, name, voida);
    } else {
        return cla;
    }
}
