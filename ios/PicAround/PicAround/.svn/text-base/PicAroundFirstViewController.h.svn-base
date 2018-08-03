//
//  PicAroundFirstViewController.h
//  PicAround
//
//  Created by BenA on 2/6/13.
//  Copyright (c) 2013 BenA. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "PicAroundApi.h"
#import "PAEventEntity.h"
#import "PicAroundAppDelegate.h"
#import "TestFlight.h"
#import "GAI.h"
@interface PicAroundFirstViewController : GAITrackedViewController<UITableViewDelegate,UITableViewDataSource,UIActionSheetDelegate>
{
    NSArray * tableViewArray;
    NSMutableDictionary *eventsDic;
    UIActionSheet *sheet;
}

- (IBAction)logout:(id)sender;
- (IBAction)menu:(id)sender;
- (void) loadEvents;
@property BOOL DidCreatedNewEvent;
@property (strong, nonatomic) UIRefreshControl *refreshControl;
@property (weak, nonatomic) IBOutlet UIActivityIndicatorView *spinner;
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (nonatomic, strong) PicAroundApi *paApi;
@property (nonatomic, retain) NSArray *tableViewArray;
@end
