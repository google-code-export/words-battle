// Copyright (c) 2012 Ilya Tetin and Danila Shikulin. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef WB_COMMON_H_
#define WB_COMMON_H_

enum LetterType {
  PASSIVE, ACTIVE
};

struct Letter {
  char value;
  float damage;
  enum LetterType type;
};

#endif  // WB_COMMON_H_
