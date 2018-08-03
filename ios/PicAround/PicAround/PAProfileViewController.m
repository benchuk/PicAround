//
//  PAProfileViewController.m
//  PicAround
//
//  Created by BenA on 2/13/13.
//  Copyright (c) 2013 BenA. All rights reserved.
//

#import "PAProfileViewController.h"
#import "PicAroundAppDelegate.h"

@interface PAProfileViewController ()

@end

@implementation PAProfileViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}
- (void) closeSession {
    //[FBSession.activeSession closeAndClearTokenInformation];
    PicAroundAppDelegate *appDelegate = [[UIApplication sharedApplication]delegate];
    [appDelegate closeSession];
    [self performSegueWithIdentifier:@"logoutnransition" sender:self];
}
- (IBAction)signout:(id)sender
{
    [self closeSession];
}
-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    
    PicAroundAppDelegate *appDelegate = [[UIApplication sharedApplication]delegate];

      
    if (appDelegate.session.isOpen) {
        [self populateUserDetails];
    }
}
- (void)viewDidLoad
{
    [super viewDidLoad];
    self.trackedViewName = @"User Profile Screen";
	// Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewDidUnload {
    [self setProfileImage:nil];
    [self setUserName:nil];
    [self setSpinner:nil];
    [self setTempLocation:nil];
    [super viewDidUnload];
}

// FBSample logic
// Displays the user's name and profile picture so they are aware of the Facebook
// identity they are logged in as.
- (void)populateUserDetails {

    self.spinner.hidesWhenStopped = YES;
    PicAroundAppDelegate *appDelegate = [[UIApplication sharedApplication]delegate];
    if (appDelegate.session.isOpen) {
        
        self.tempLocation.text = [[appDelegate.Latitude stringByAppendingString:@" , "] stringByAppendingString:appDelegate.Longitude];
        [self.spinner startAnimating];
        self.userName.text = appDelegate.user.name;
        self.profileImage.profileID = appDelegate.user.id;
        [self.spinner stopAnimating];
       
    }
}

@end
