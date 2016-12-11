//
//  MCGLContext.c
//  monkcGame
//
//  Created by SunYuLi on 16/2/23.
//  Copyright © 2016年 oreisoft. All rights reserved.
//

#include "MCGLContext.h"
#include "MC3DBase.h"
#include "MCGLEngine.h"
#include "BEAssetsManager.h"
#include "MCGLBase.h"

oninit(MCGLContext)
{
    if (init(MCObject)) {
        var(pid) = glCreateProgram();
        var(uniformCount) = 0;
        return obj;
    }else{
        return null;
    }
}

function(int, fillUniformLocation, MCGLUniform* uniform)
{
    as(MCGLContext);
    if (uniform->location == MC3DErrUniformNotFound) {
        uniform->location = glGetUniformLocation(var(pid), uniform->name);
    }
    return uniform->location;
}

method(MCGLContext, MCGLContext*, initWithShaderCode, const char* vcode, const char* fcode,
       const char* attribs[], size_t acount, MCGLUniformType types[], const char* uniforms[], size_t ucount)
{
    MCGLEngine_tryUseShaderProgram(obj->pid);

    //attribute
    for (int i=0; i<acount; i++) {
        glBindAttribLocation(obj->pid, i, attribs[i]);
    }
    
    MCGLEngine_prepareShader(obj->pid, vcode, fcode);

    if(MCGLEngine_validateProgram(var(pid))) {
        error_log("prepare shader program failed\n");
        exit(0);
    }

    //uniforms
    for (int i=0; i<ucount; i++) {
        MCGLUniform* f = &obj->uniforms[obj->uniformCount++];
        MCGLUniformSetName(f, uniforms[i]);
        f->type = types[i];
        f->location = glGetUniformLocation(var(pid), uniforms[i]);
        obj->uniformsDirty[i] = false;
    }
    
    return obj;
}

method(MCGLContext, MCGLContext*, initWithShaderName, const char* vname, const char* fname,
       const char* attribs[], size_t acount, MCGLUniformType types[], const char* uniforms[], size_t ucount)
{
    char path[LINE_MAX];
    MCFileGetPath(vname, "vsh", path);
    const char* vcode = MCFileCopyContentWithPath(path);
    
    MCFileGetPath(vname, "fsh", path);
    const char* fcode = MCFileCopyContentWithPath(path);
    
    MCGLContext_initWithShaderCode(0, obj, vcode, fcode, attribs, acount, types, uniforms, ucount);
    
    free((void*)vcode);
    free((void*)fcode);
    return obj;
}

method(MCGLContext, void, activateShaderProgram, voida)
{
    MCGLEngine_tryUseShaderProgram(var(pid));
}

method(MCGLContext, int, getUniformLocation, const char* name)
{
    for (MCUInt i=0; i<obj->uniformCount; i++) {
        if (strcmp(name, obj->uniforms[i].name)==0) {
            return fillUniformLocation(0, obj, &obj->uniforms[i]);
        }
    }
    return MC3DErrUniformNotFound;
}

//MCGLUniformScalar,
//MCGLUniformVec2,
//MCGLUniformVec3,
//MCGLUniformVec4,
//MCGLUniformMat3,
//MCGLUniformMat4
function(int, setUniform, const char* name, int loc, MCGLUniform* uniform)
{
    as(MCGLContext);
    if (var(pid) == 0) {
        return -1;
    }
    if (name != null) {
        loc = glGetUniformLocation(var(pid), name);
    }
    if (loc != MC3DErrUniformNotFound) {
        switch (uniform->type) {
            case MCGLUniformScalar:
                glUniform1i(loc, uniform->data.scalar);
                break;
            case MCGLUniformVec1:
                glUniform1f(loc, uniform->data.vec1);
                break;
            case MCGLUniformVec2:
                glUniform2f(loc, uniform->data.vec2.x, uniform->data.vec2.y);
                break;
            case MCGLUniformVec3:
                glUniform3f(loc, uniform->data.vec3.x, uniform->data.vec3.y,
                            uniform->data.vec3.z);
                break;
            case MCGLUniformVec4:
                glUniform4f(loc, uniform->data.vec4.x, uniform->data.vec4.y,
                            uniform->data.vec4.z, uniform->data.vec4.w);
                break;
            case MCGLUniformMat3:
                glUniformMatrix3fv(loc, 1, 0, uniform->data.mat3.m);
                break;
            case MCGLUniformMat4:
                glUniformMatrix4fv(loc, 1, 0, uniform->data.mat4.m);
                break;
            default:
                break;
        }
    }
    return loc;
}

method(MCGLContext, void, updateUniform, const char* name, MCGLUniformData udata)
{
    MCGLUniform* u = null;
    int f = -1;
    for (int i=0; i<var(uniformCount); i++) {
        u = &var(uniforms)[i];
        if (strcmp(name, u->name) == 0) {
            f = i;
            break;
        }
    }
    
    if (u != null && f != -1 && !MCGLUniformDataEqual(u->type, &u->data, &udata)) {
        var(uniformsDirty)[f] = true;
        var(uniforms)[f].data = udata;
    }
    else if (f != -1){
        var(uniformsDirty)[f] = false;
    }
}

method(MCGLContext, void,  setUniforms, voida)
{
    for (int i=0; i<var(uniformCount); i++) {
        //if (var(uniformsDirty)[i] == true) {
            MCGLUniform* f = &var(uniforms)[i];
            setUniform(0, obj, null, f->location, f);
            var(uniformsDirty)[i] = false;
        //}
    }
    
}

method(MCGLContext, int, getUniformVector, const char* name, GLfloat* params)
{
    int loc = (int)ff(obj, getUniformLocation, name);
    glGetUniformfv(var(pid), loc, params);
    return loc;
}

/*
struct View {
    mat4 view;
    mat4 projection;
    vec3 position;
};
struct Model {
    mat4 model;
    mat3 normal;
};
struct Light {
    lowp vec3 ambient;
    lowp vec3 diffuse;
    lowp vec3 specular;
    lowp vec3 color;
    lowp vec3 position;
};
struct Material {
    lowp vec3 ambient;
    lowp vec3 diffuse;
    lowp vec3 specular;
    lowp float dissolve;
    lowp float shininess;
};
*/

method(MCGLContext, void, printUniforms, voida)
{
    MCLogTypeSet(MC_DEBUG);

    GLfloat light_ambient[3];
    GLfloat light_diffuse[3];
    GLfloat light_specular[3];
    GLfloat light_color[3];
    GLfloat light_position[3];

    GLfloat material_ambient[3];
    GLfloat material_diffuse[3];
    GLfloat material_specular[3];
    GLfloat material_dissolve;
    GLfloat material_shininess;

    ff(obj, getUniformVector, "light.ambient", light_ambient);
    ff(obj, getUniformVector, "light.diffuse", light_diffuse);
    ff(obj, getUniformVector, "light.specular", light_specular);
    ff(obj, getUniformVector, "light.color", light_color);
    ff(obj, getUniformVector, "light.position", light_position);

    ff(obj, getUniformVector, "material.ambient", material_ambient);
    ff(obj, getUniformVector, "material.diffuse", material_diffuse);
    ff(obj, getUniformVector, "material.specular", material_specular);
    ff(obj, getUniformVector, "material.dissolve", &material_dissolve);
    ff(obj, getUniformVector, "material.shininess", &material_shininess);

    debug_log("light.ambient  %f/%f/%f\n", light_ambient[0], light_ambient[1], light_ambient[2]);
    debug_log("light.diffuse  %f/%f/%f\n", light_diffuse[0], light_diffuse[1], light_diffuse[2]);
    debug_log("light.specular  %f/%f/%f\n", light_specular[0], light_specular[1], light_specular[2]);
    debug_log("light.color  %f/%f/%f\n", light_color[0], light_color[1], light_color[2]);
    debug_log("light.position  %f/%f/%f\n", light_position[0], light_position[1], light_position[2]);

    debug_log("material.ambient  %f/%f/%f\n", material_ambient[0], material_ambient[1], material_ambient[2]);
    debug_log("material.diffuse  %f/%f/%f\n", material_diffuse[0], material_diffuse[1], material_diffuse[2]);
    debug_log("material.specular  %f/%f/%f\n", material_specular[0], material_specular[1], material_specular[2]);
    debug_log("material.dissolve  %f\n", material_dissolve);
    debug_log("material.shininess  %f\n", material_shininess);

}

onload(MCGLContext)
{
    if (load(MCObject)) {
        mixing(int, setUniform, const char* name, int loc, MCGLUniform* uniform);

        binding(MCGLContext, void, activateShaderProgram, voida);
        
        binding(MCGLContext, MCGLContext*, initWithShaderCode, const char* vcode, const char* fcode,
               const char* attribs[], size_t acount, MCGLUniformType types[], const char* uniforms[], size_t ucount);
        binding(MCGLContext, MCGLContext*, initWithShaderName, const char* vname, const char* fname,
                const char* attribs[], size_t acount, MCGLUniformType types[], const char* uniforms[], size_t ucount);
        
        binding(MCGLContext, void, updateUniform, const char* name, MCGLUniformData udata);
        binding(MCGLContext, void, setUniforms, voida);
        binding(MCGLContext, int,  getUniformVector,  const char* name, GLfloat* params);
        binding(MCGLContext, int,  getUniformLocation, const char* name);
        binding(MCGLContext, void, printUniforms, voida);

        return cla;
    }else{
        return null;
    }
}
