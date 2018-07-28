//
//  MCTextureCache.h
//  BohdiEngine
//
//  Created by 孙御礼 on 2/26/30 H.
//

#ifndef MCTextureCache_h
#define MCTextureCache_h

#include "monkc_export.h"
#include "MCTexture.h"

class(MCTextureCache, MCObject,
      MCMap* map);

utility(MCTextureCache, MCTextureCache*, shared, voida);
utility(MCTextureCache, void, cleanAndDestoryShared, voida);

method(MCTextureCache, void, bye, voida);
method(MCTextureCache, MCTexture*, findTextureNamed, const char* name);
method(MCTextureCache, void, cacheTextureNamed, MCTexture* tex, const char* name);

#endif /* MCTextureCache_h */
