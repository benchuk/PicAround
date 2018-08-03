//
//  PALoginViewController.h
//  PicAround
//
//  Created by BenA on 2/7/13.
//  Copyright (c) 2013 BenA. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "GAITrackedViewController.h"
#import "GAI.h"
@interface PALoginViewController : GAITrackedViewController
- (IBAction)authButtonAction:(id)sender;
@property (weak, nonatomic) IBOutlet UITextView *loginMessage;
@property (weak, nonatomic) IBOutlet UIButton *authButton;
-(void) transition;
@end
