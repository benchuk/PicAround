//
//  MPACaptionView.m
//  PicAround
//
//  Created by BenA on 5/27/13.
//  Copyright (c) 2013 BenA. All rights reserved.
//

#import "MPACaptionView.h"
#import <QuartzCore/QuartzCore.h>

static const CGFloat labelPadding = 10;
// Private
@interface MPACaptionView () {
    id<MWPhoto> _photo;
    UILabel *_label;
}
@end
@implementation MPACaptionView

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    return self;
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

// To create your own custom caption view, subclass this view
// and override the following two methods (as well as any other
// UIView methods that you see fit):

// Override -setupCaption so setup your subviews and customise the appearance
// of your custom caption
// You can access the photo's data by accessing the _photo ivar
// If you need more data per photo then simply subclass MWPhoto and return your
// subclass to the photo browsers -photoBrowser:photoAtIndex: delegate method
- (void)setupCaptionCustomLabel:(NSString*) textForLabel subText:(NSString *) subTextForLabel{
    
    //self.labelCustom = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, self.bounds.size.width-labelPadding*2, self.bounds.size.height)];
    self.labelCustom = [[UILabel alloc] initWithFrame:CGRectMake(90, 30, self.bounds.size.width-labelPadding*2 - 20,20)];
    self.labelCustom.autoresizingMask = UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight;
    self.labelCustom.opaque = NO;
    self.labelCustom.backgroundColor = [UIColor clearColor];
    self.labelCustom.textAlignment = UITextAlignmentLeft;
    self.labelCustom.lineBreakMode = UILineBreakModeWordWrap;
    self.labelCustom.numberOfLines = 3;
    self.labelCustom.textColor = [UIColor whiteColor];
    self.labelCustom.shadowColor = [UIColor blackColor];
    self.labelCustom.shadowOffset = CGSizeMake(1, 1);
    self.labelCustom.font = [UIFont fontWithName:@"AppleSDGothicNeo-Bold" size:20];
    self.labelCustom.text =textForLabel;
   
    
    self.subLabelCustom = [[UILabel alloc] initWithFrame:CGRectMake(90, 50, self.bounds.size.width-labelPadding*2 - 20,20)];
    self.subLabelCustom.autoresizingMask = UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight;
    self.subLabelCustom.opaque = NO;
    self.subLabelCustom.backgroundColor = [UIColor clearColor];
    self.subLabelCustom.textAlignment = UITextAlignmentLeft;
    self.subLabelCustom.lineBreakMode = UILineBreakModeWordWrap;
    self.subLabelCustom.numberOfLines = 3;
    self.subLabelCustom.textColor = [UIColor grayColor];
    self.subLabelCustom.shadowColor = [UIColor blackColor];
    self.subLabelCustom.shadowOffset = CGSizeMake(1, 1);
    self.subLabelCustom.font = [UIFont fontWithName:@"AppleSDGothicNeo-Bold" size:14];
    self.subLabelCustom.text = subTextForLabel;

    [self addSubview:self.subLabelCustom];
    [self addSubview:self.labelCustom];
}

- (void)setupCaptionCustomImage:(UIImageView *)imageView {
    
    self.imageView = imageView;
    self.imageView.frame = CGRectMake(0,10,70,70);
//    self.imageView.layer.borderColor = [[UIColor whiteColor] CGColor];
//    self.imageView.layer.borderWidth = 1;
    
        self.imageView.clipsToBounds = YES;
        self.imageView.layer.cornerRadius = 35.0;
        self.imageView.layer.masksToBounds = YES;
    
    self.imageView.contentMode = UIViewContentModeScaleAspectFit;
    [self addSubview:self.imageView];
    
}

// Override -sizeThatFits: and return a CGSize specifying the height of your
// custom caption view. With width property is ignored and the caption is displayed
// the full width of the screen
- (CGSize)sizeThatFits:(CGSize)size
{
    CGFloat maxHeight = 9999;
    if (self.labelCustom.numberOfLines > 0) maxHeight = self.labelCustom.font.leading*self.labelCustom.numberOfLines;
    CGSize textSize = [_label.text sizeWithFont:self.labelCustom.font
                              constrainedToSize:CGSizeMake(size.width - labelPadding*2, maxHeight)
                                  lineBreakMode:self.labelCustom.lineBreakMode];
    return CGSizeMake(size.width, 90);

}

@end
