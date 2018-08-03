//
//  EventCell.m
//  PicAround
//
//  Created by BenA on 2/16/13.
//  Copyright (c) 2013 BenA. All rights reserved.
//

#import "EventCell.h"
#import <QuartzCore/QuartzCore.h>
@implementation EventCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
        
    }
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    
        self.IsSelectedIndication.hidden = !selected;
    
}

-(void) layoutSubviews
{
    //[super layoutSubviews];
    //self.eventName.frame = CGRectMake(10,73,198,20);
    //self.eventLocation.frame = CGRectMake(10,73,198,37);
    //self.placeHolder.frame = CGRectMake(10,10,60,60);
    self.placeHolder.clipsToBounds = YES;
    //self.placeHolder.layer.cornerRadius = 7.0;
    //self.placeHolder.layer.masksToBounds = YES;
    //self.placeHolder.layer.borderColor = [UIColor grayColor].CGColor;
    //self.placeHolder.layer.borderWidth = 1.0;
    self.backPanel.layer.shadowColor = [UIColor blackColor].CGColor;
    self.backPanel.layer.shadowOffset = CGSizeMake(1, 1);
    self.backPanel.clipsToBounds = YES;
    //self.backPanel.layer.cornerRadius = 5.0;
    self.backPanel.layer.masksToBounds = YES;
  
    
    //self.imageView.frame = CGRectMake(10,10,60,60);
    self.imageView.clipsToBounds = YES;
    
    self.imageView.layer.shadowColor = [UIColor blackColor].CGColor;
    self.imageView.layer.shadowOffset = CGSizeMake(-2, -2);
    //self.imageView.layer.cornerRadius = 3.0;
    //self.imageView.layer.masksToBounds = YES;
//    self.imageView.layer.borderColor = [UIColor whiteColor].CGColor;
//    self.imageView.layer.borderWidth = 1.0;
}



@end
