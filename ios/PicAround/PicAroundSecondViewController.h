//
//  PicAroundSecondViewController.h
//  PicAround
//
//  Created by BenA on 2/6/13.
//  Copyright (c) 2013 BenA. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface PicAroundSecondViewController : UIViewController<UIImagePickerControllerDelegate>
{

    IBOutlet UIImageView *imageView;
    IBOutlet UIButton *Camera;
}


-(IBAction)getCameraPicture:(id)sender;
-(IBAction)selectExistingPicture;



@end

