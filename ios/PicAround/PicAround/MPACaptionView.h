//
//  MPACaptionView.h
//  PicAround
//
//  Created by BenA on 5/27/13.
//  Copyright (c) 2013 BenA. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MWCaptionView.h"

@interface MPACaptionView : MWCaptionView
@property (strong) UIImageView* imageView;
@property (strong) UILabel *labelCustom;
@property (strong) UILabel *subLabelCustom;

- (void)setupCaptionCustomImage:(UIImageView *)imageView;
- (void)setupCaptionCustomLabel:(NSString*) textForLabel subText:(NSString *) subTextForLabel;
@end
